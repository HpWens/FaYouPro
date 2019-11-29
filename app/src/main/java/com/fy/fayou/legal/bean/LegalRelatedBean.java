package com.fy.fayou.legal.bean;

import java.io.Serializable;
import java.util.List;

public class LegalRelatedBean implements Serializable {

    public List<LegalEntity> judicialInterpretations;
    public List<LegalEntity> lawBindCaseAOs;
    public List<LegalEntity> lawBindJudgeAOs;

}
