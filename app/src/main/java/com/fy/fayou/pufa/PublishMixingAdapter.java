package com.fy.fayou.pufa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fy.fayou.R;
import com.vondear.rxtool.RxDeviceTool;
import com.vondear.rxtool.RxImageTool;

import java.util.ArrayList;
import java.util.List;

public class PublishMixingAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context mContext;
    private List<Object> mDataList = new ArrayList<>();

    private int mCurrentFocusPos = -1;

    private int mMaxCharLength = 5000;

    // 文本 item
    public static final int TEXT_ITEM_TYPE = 0x000111;
    // 图片 item
    public static final int PIC_ITEM_TYPE = 0x000222;
    // 标题 title
    public static final int TITLE_ITEM_TYPE = 0x000333;

    // 连续2次点击删除图片
    private int mDoubleDeleteIndex = 0;

    private OnMixingListener mListener;

    private int mPicWidth;

    public PublishMixingAdapter(Context context, OnMixingListener listener) {
        mListener = listener;
        mContext = context;
        mPicWidth = RxDeviceTool.getScreenWidth(context) - RxImageTool.dip2px(15);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.size() > position) {
            Object obj = mDataList.get(position);
            if (obj instanceof TextEntity) {
                return TEXT_ITEM_TYPE;
            } else if (obj instanceof PicEntity) {
                return PIC_ITEM_TYPE;
            } else if (obj instanceof TitleEntity) {
                return TITLE_ITEM_TYPE;
            }
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TEXT_ITEM_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publish_text, parent, false);
        } else if (viewType == PIC_ITEM_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publish_pic, parent, false);
        } else if (viewType == TITLE_ITEM_TYPE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publish_title, parent, false);
        }
        return view == null ? null : new BaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int type = getItemViewType(holder.getAdapterPosition());
        if (type == TITLE_ITEM_TYPE) {
            holder.itemView.setTag("title");
            final EditText etTitle = holder.getView(R.id.et_title);
            Object obj = mDataList.get(holder.getAdapterPosition());
            if (!(obj instanceof TitleEntity)) return;

            TitleEntity bean = (TitleEntity) obj;
            etTitle.setText(bean.name);

            final TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    saveTitleToData(s.toString(), holder.getAdapterPosition());

                    if (mListener != null) {
                        mListener.onEditTextChanged();
                    }
                }
            };

            etTitle.setOnFocusChangeListener((v, hasFocus) -> {

                if (mListener != null) {
                    mListener.onFocusChange(hasFocus, holder.getAdapterPosition());
                }

                if (hasFocus) {
                    etTitle.addTextChangedListener(textWatcher);
                } else {
                    etTitle.removeTextChangedListener(textWatcher);
                }
            });


        } else if (type == TEXT_ITEM_TYPE) {
            holder.itemView.setTag("text");
            final EditText etInput = holder.getView(R.id.et_input);
            Object obj = mDataList.get(holder.getAdapterPosition());
            if (!(obj instanceof TextEntity)) return;

            // 设置输入的内容
            TextEntity bean = (TextEntity) obj;
            etInput.setHint(bean.hint);
            etInput.setHint(bean.hint);
            etInput.setText(bean.content);

            final TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString();
                    saveTextToData(text, holder.getAdapterPosition());

                    int charLength = getCharLength();

                    if (charLength > mMaxCharLength) {
                        int limit = charLength - mMaxCharLength;
                        if (limit <= text.length()) {
                            s.delete(text.length() - limit, text.length());
                            saveTextToData(s.toString(), holder.getAdapterPosition());
                            // 暴露接口
                            if (mListener != null) {
                                mListener.onMaxCharLengthLimit();
                            }
                        }
                    }

                    // 重置状态
                    if (s.length() > 0 && mDoubleDeleteIndex != 0) {
                        resetDoubleDelete();
                    }

                    if (mListener != null) {
                        mListener.onEditTextChanged();
                    }
                }
            };

            etInput.setOnFocusChangeListener((v, hasFocus) -> {
                if (mListener != null) {
                    mListener.onFocusChange(hasFocus, holder.getAdapterPosition());
                }
                if (hasFocus) {
                    mCurrentFocusPos = holder.getAdapterPosition();
                    etInput.addTextChangedListener(textWatcher);
                } else {
                    etInput.removeTextChangedListener(textWatcher);
                }
            });

            etInput.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    if (mListener != null) {
                        mListener.onFocusChange(true, holder.getAdapterPosition());
                    }
                    // 重置选中状态
                    if (mListener != null && mCurrentFocusPos != holder.getAdapterPosition() && mDoubleDeleteIndex != 0) {
                        resetDoubleDelete();
                    }

                    mCurrentFocusPos = holder.getAdapterPosition();
                }
                return false;
            });


            etInput.setOnKeyListener((v, keyCode, event) -> {

                Editable editable = etInput.getText();
                // 手势处理
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (editable.length() == 0 && (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_FORWARD_DEL)) {
                        mDoubleDeleteIndex++;
                        if (mDoubleDeleteIndex >= 2) {
                            mDoubleDeleteIndex = 2;
                        }
                        // 第一次选中
                        if (mDoubleDeleteIndex == 1) {
                            if (mListener != null) {

                            }
                        } else {
                            // 第二次删除
                            if (mListener != null && holder.getAdapterPosition() > 0) {

                            }
                            mDoubleDeleteIndex = 0;
                        }
                    }
                }
                return false;
            });

//            if (mCurrentFocusPos == holder.getAdapterPosition()) {
//                if (bean.isFocused) {
//                    bean.isFocused = false;
//                    etInput.requestFocus();
//                }
//                etInput.setSelection(etInput.length());
//            }

        } else if (type == PIC_ITEM_TYPE) {
            holder.itemView.setTag("pic");
            Object obj = mDataList.get(holder.getAdapterPosition());
            if (!(obj instanceof PicEntity)) return;
            final PicEntity picEntity = (PicEntity) obj;

            ImageView ivPic = holder.getView(R.id.iv_picture);
            Glide.with(mContext)
                    .load(picEntity.path)
                    .override(mPicWidth, mPicWidth)
                    .into(ivPic);

            ImageView ivDelete = holder.getView(R.id.iv_delete);
            ivDelete.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onDeleteSinglePicture(v, holder.getAdapterPosition());
                }
            });

            View stroke = holder.getView(R.id.iv_delete_stroke);
            stroke.setVisibility(picEntity.isSelected ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                // 进入图片预览页

            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private void saveTitleToData(String title, int position) {
        if (position != -1) {
            Object obj = mDataList.get(position);
            if (null != obj && obj instanceof TitleEntity) {
                TitleEntity entity = (TitleEntity) obj;
                entity.name = title;
                mDataList.set(position, entity);
            }
        }
    }

    private void saveTextToData(String text, int position) {
        if (position != -1) {
            Object obj = mDataList.get(position);
            if (null != obj && obj instanceof TextEntity) {
                TextEntity entity = (TextEntity) obj;
                entity.content = text;
                mDataList.set(position, entity);
            }
        }
    }

    private int getCharLength() {
        int length = 0;
        for (Object obj : mDataList) {
            if (obj instanceof TextEntity) {
                TextEntity entity = (TextEntity) obj;
                if (null != entity.content) {
                    length += entity.content.trim().length();
                }
            }
        }
        return length;
    }

    private void resetDoubleDelete() {
        mDoubleDeleteIndex = 0;
        if (mCurrentFocusPos == -1) return;
        if (mListener != null) {

        }
    }

    public int getCurrentFocusPos() {
        return mCurrentFocusPos;
    }

    public void setNewData(List<Object> data) {
        if (null != data) {
            this.mDataList = data;
            notifyDataSetChanged();
        }
    }

    public List<Object> getData() {
        return mDataList;
    }

    public interface OnMixingListener {
        // 最大字数限制
        void onMaxCharLengthLimit();

        void onDeleteSinglePicture(View v, int position);

        void onFocusChange(boolean focus, int position);

        void onEditTextChanged();
    }

}
