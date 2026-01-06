package com.attendance.project.financial.service.impl;

import com.attendance.common.utils.text.Convert;
import com.attendance.project.financial.domain.*;
import com.attendance.project.financial.mapper.*;
import com.attendance.project.financial.service.IFinSalRecService;
import com.attendance.project.system.line.domain.RecLine;
import com.attendance.project.system.user.domain.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工资记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-21
 */
@Service
@Transactional
public class FinSalRecServiceImpl implements IFinSalRecService
{
    @Autowired
    private FinSalMapper finSalMapper;

    @Autowired
    private FinSalRecMapper finSalRecMapper;

    @Autowired
    private FinOvertimeMapper finOvertimeMapper;

    @Autowired
    private FinOvertimeDetailMapper finOvertimeDetailMapper;

    @Autowired
    private FinUserParamMapper finUserParamMapper;

    @Autowired
    private FinParamMapper finParamMapper;

    @Autowired
    private FinParamDetailMapper finParamDetailMapper;

    @Autowired
    private FinKpiDetailMapper finKpiDetailMapper;

    @Autowired
    private FinDeductDetailMapper finDeductDetailMapper;

    @Override
    public FinSal selectFinSalById(Long id) {
        return finSalMapper.selectFinSalById(id);
    }

    @Override
    public int updateFinSal(FinSal finSal) {
        return finSalMapper.updateFinSal(finSal);
    }

    /**
     * 查询工资记录
     * 
     * @param id 工资记录主键
     * @return 工资记录
     */
    @Override
    public FinSalRec selectFinSalRecById(Long id)
    {
        return finSalRecMapper.selectFinSalRecById(id);
    }

    /**
     * 查询工资记录列表
     * 
     * @param finSalRec 工资记录
     * @return 工资记录
     */
    @Override
    public List<FinSalRec> selectFinSalRecList(FinSalRec finSalRec)
    {
        return finSalRecMapper.selectFinSalRecList(finSalRec);
    }

    /**
     * 新增工资记录
     * 
     * @param finSalRec 工资记录
     * @return 结果
     */
    @Override
    public int insertFinSalRec(FinSalRec finSalRec)
    {
        return finSalRecMapper.insertFinSalRec(finSalRec);
    }

    /**
     * 修改工资记录
     * 
     * @param finSalRec 工资记录
     * @return 结果
     */
    @Override
    public int updateFinSalRec(FinSalRec finSalRec)
    {
        return finSalRecMapper.updateFinSalRec(finSalRec);
    }

    /**
     * 批量删除工资记录
     * 
     * @param ids 需要删除的工资记录主键
     * @return 结果
     */
    @Override
    public int deleteFinSalRecByIds(String ids)
    {
        return finSalRecMapper.deleteFinSalRecByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除工资记录信息
     * 
     * @param id 工资记录主键
     * @return 结果
     */
    @Override
    public int deleteFinSalRecById(Long id)
    {
        return finSalRecMapper.deleteFinSalRecById(id);
    }

    @Override
    public int insertFinSal(FinSal finSal) {
        return finSalMapper.insertFinSal(finSal);
    }

    @Override
    public List<FinSal> selectFinSalList(FinSal finSal) {
        return finSalMapper.selectFinSalList(finSal);
    }

    @Override
    public int deleteFinSalByIds(String ids) {
        return finSalMapper.deleteFinSalByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteFinSalRecBySalids(String ids) {
        return finSalRecMapper.deleteFinSalRecBySalids(Convert.toStrArray(ids));
    }

    @Override
    public int deleteFinSalRecOverview(FinSalRec finSalRec) {
        return finSalRecMapper.deleteFinSalRecOverview(finSalRec);
    }

    @Override
    public int batchInsertFinSalRec(List<FinSalRec> finSalRecList) {
        return finSalRecMapper.batchInsertFinSalRec(finSalRecList);
    }

    @Override
    public FinSalRec calcUserFinSalRec(User user, FinSalRec finSalRec, List<FinParam> finParams, List<FinOvertime> overtimes, List<FinKpi> kpis, List<FinDeduct> deducts) throws Exception {
        //获取前月与后月，扩大工资记录检索范围
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        String timetag =  finSalRec.getTimetag();
        Date timetagDate = df.parse(timetag);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timetagDate);
        calendar.add(Calendar.MONTH,-2);
        String preDateStr = df.format(calendar.getTime());
        calendar.add(Calendar.MONTH,1);
        String nextDateStr = df.format(calendar.getTime());

        //获取基本数值
        Long userid = user.getUserId();
        FinUserParam finUserParam = new FinUserParam();
        finUserParam.setUserId(userid);
        List<FinUserParam> finUserParams = finUserParamMapper.selectFinUserParamList(finUserParam);
        if (finUserParams.size() > 0) finUserParam = finUserParams.get(0);
        String nggz = finUserParam.getNggz()!=null?finUserParam.getNggz():"";
        String gwgz = finUserParam.getGwgz()!=null?finUserParam.getGwgz():"";
        String paramRank = finUserParam.getParamRank()!=null?finUserParam.getParamRank():"";
        String paramPost = finUserParam.getParamPost()!=null?finUserParam.getParamPost():"";
        String dzf = finUserParam.getDzf()!=null?finUserParam.getDzf():"";
        String sfhy = finUserParam.getSfhy()!=null?finUserParam.getSfhy():"";
        String yhkh = finUserParam.getYhkh()!=null?finUserParam.getYhkh():"";
        finSalRec.setYhkh(yhkh);

        //加班天数
        String shxts = "0";
        String sxts = "0";
        String jbts = "0";
        String ybts = "0";

        if (overtimes != null && overtimes.size() > 0) {
            int two = 0;
            int three = 0;
            int one = 0;
            int night = 0;
            for (int i = 0; i < overtimes.size(); i++) {
                FinOvertime overtime = overtimes.get(i);
                FinOvertimeDetail detail = new FinOvertimeDetail();
                detail.setOid(overtime.getId());
                detail.setUserid(userid);
                if (overtime.getRecname().indexOf("补")>=0){

                } else {
                    Map<String, Object> overtimeParams = new HashMap<String, Object>();
                    overtimeParams.put("beginTime", preDateStr + "-21");
                    overtimeParams.put("endTime", nextDateStr + "-20");
                    detail.setParams(overtimeParams);
                }
                detail.setOvertype("two");
                List<FinOvertimeDetail> details2 = finOvertimeDetailMapper.selectFinOvertimeDetailList(detail);
                two += details2.size();
                detail.setOvertype("three");
                List<FinOvertimeDetail> details3 = finOvertimeDetailMapper.selectFinOvertimeDetailList(detail);
                three += details3.size();
                detail.setOvertype("one");
                List<FinOvertimeDetail> details1 = finOvertimeDetailMapper.selectFinOvertimeDetailList(detail);
                one += details1.size();
                if (overtime.getRectime().equals(nextDateStr)){
                    detail.setOvertype("night");
                    detail.setParams(null);
                    List<FinOvertimeDetail> detailsN = finOvertimeDetailMapper.selectFinOvertimeDetailList(detail);
                    if (detailsN.size() > 0) {
                        night += Convert.toInt(detailsN.get(0).getOverdays(), 0);
                    }
                }
            }
            shxts = String.valueOf(two);
            sxts = String.valueOf(three);
            jbts = String.valueOf(one);
            ybts = String.valueOf(night);
        }

        //个人职级岗位参数
        String jxgz2 = "";
        String fsjwf = "";
        String qnf = "";
        if (finParams != null && finParams.size() > 0) {
            FinParam finParam = finParams.get(0);
            //获取配置参数
            FinParamDetail finParamDetail = new FinParamDetail();
            finParamDetail.setPid(finParam.getId());
            finParamDetail.setDetailCode("jxgz2");
            finParamDetail.setDetailKey(paramRank);
            jxgz2 = finParamDetailMapper.selectFinParamDetailByCodeKey(finParamDetail);
            if (jxgz2 != null) {
                finSalRec.setJxgz2(jxgz2);
            } else {
                finSalRec.setJxgz2("");
            }
            finParamDetail.setDetailCode("fsjwf");
            finParamDetail.setDetailKey(paramPost);
            fsjwf = finParamDetailMapper.selectFinParamDetailByCodeKey(finParamDetail);
            if (fsjwf != null && (timetag.contains("-07") || timetag.contains("-08") || timetag.contains("-09") || timetag.contains("-10"))) {
                finSalRec.setFsjwf(fsjwf);
            } else {
                finSalRec.setFsjwf("");
            }
            finParamDetail.setDetailCode("qnf");
            finParamDetail.setDetailKey(paramRank);
            qnf = finParamDetailMapper.selectFinParamDetailByCodeKey(finParamDetail);
            if (qnf != null && timetag.contains("-12")) {
                finSalRec.setQnf(qnf);
            } else {
                finSalRec.setQnf("");
            }
        }

        finSalRec.setNggz(nggz);
        finSalRec.setPostValue(gwgz);
        finSalRec.setDzf(dzf);

        Double gzxx2 = Convert.toDouble(finSalRec.getGzxs(), 0.0, 2); //工资系数
        Double nggz2 = Convert.toDouble(nggz, 0.0, 2); //年功工资
        Double gwgz2 = Convert.toDouble(gwgz, 0.0, 2); //岗位工资
        Double yxqxed2 = Convert.toDouble(finSalRec.getYxqxed(), 0.0, 2); //一线额度
        Double gdjj2 = Convert.toDouble(finSalRec.getGdjj(), 0.0, 2); //固定奖金
        Double dzf2 = Convert.toDouble(dzf, 0.0, 2); //独子费
        Double wybt2 = Convert.toDouble(finSalRec.getWybt(), 0.0, 2); //物业补贴
        //住房补贴
        Double zfbt = Convert.simpDouble((nggz2 + gwgz2) * 0.35, 2);
        finSalRec.setZfbt(zfbt.toString());
        //岗位效益
        Double gwxy = Convert.simpDouble(gwgz2 * gzxx2, 2);
        finSalRec.setGwxy(gwxy.toString());
        //住房补贴效益
        Double zfbtxy = Convert.simpDouble(gwxy * 0.35, 2);
        finSalRec.setZfbtxy(zfbtxy.toString());

        //个人效益工资总额
        Double grxyze = gwxy + zfbtxy + gdjj2;
        finSalRec.setGrxyze(grxyze.toString());

        //部门奖励绩效
        String bmjxdj = "A";
        String grjljx = "";
        String grkfjx = "";
        if (kpis != null && kpis.size() > 0) {
            FinKpi kpi = kpis.get(kpis.size() - 1);
            FinKpiDetail detail = new FinKpiDetail();
            detail.setPid(kpi.getId());
            detail.setUserid(userid);
            List<FinKpiDetail> details = finKpiDetailMapper.selectFinKpiDetailList(detail);
            if (details.size() > 0) {
                detail = details.get(details.size() - 1);
                bmjxdj = detail.getJxdj();
                grjljx = detail.getJljx();
                grkfjx = detail.getKfjx();
            }
        }
        //部门绩效等级暂存为rankcode
        finSalRec.setRankCode(bmjxdj);
        finSalRec.setGrjljx(grjljx);
        finSalRec.setGrkfjx(grkfjx);

        String kqdj = finSalRec.getKqdj();
        //绩效对比取低
        if (bmjxdj.compareTo(kqdj) > 0) {
            kqdj = bmjxdj;
        }
        Double kqdjValue = 1.05;
        if ("A".equals(kqdj)) {
            kqdjValue = 1.05;
        } else if ("B".equals(kqdj)) {
            kqdjValue = 1.0;
        } else if ("C".equals(kqdj)) {
            kqdjValue = 0.99;
        } else if ("D".equals(kqdj)) {
            kqdjValue = 0.98;
        } else if ("E".equals(kqdj)) {
            kqdjValue = 0.97;
        }
        Double bmjljx = Convert.simpDouble(grxyze * 0.4 * (kqdjValue - 1), 2);
        finSalRec.setBmjljx(bmjljx.toString());
        Double grjljx2 = Convert.toDouble(grjljx, 0.0, 2);
        Double grkfjx2 = Convert.toDouble(grkfjx, 0.0, 2);
        //实发效益工资
        Double sfxygz = Convert.simpDouble(grxyze + bmjljx + grjljx2 - grkfjx2, 2);
        finSalRec.setSfxygz(sfxygz.toString());
        //绩效工资2
        Double jxgz22 = Convert.toDouble(finSalRec.getJxgz2(), 0.0, 2);
        //应发小计
        Double yfxj = Convert.simpDouble(nggz2 + yxqxed2 + gwgz2 + zfbt + dzf2 + sfxygz + jxgz22, 2);
        finSalRec.setYfxj(yfxj.toString());
        //计算加班补助
        //基数
        Double bzjs = Convert.simpDouble(nggz2 + yxqxed2 + gwgz2 + zfbt, 2);
        Double threebz = Convert.simpDouble(bzjs / 21.75 * 3 * Convert.toInt(sxts, 0), 2);
        Double twobz = Convert.simpDouble(bzjs / 21.75 * 2 * Convert.toInt(shxts, 0), 2);
        Double jbbz = Convert.simpDouble(bzjs / 21.75 * 1.5 * Convert.toInt(jbts, 0), 2);
        Double ybbz = Convert.simpDouble(25.0 * Convert.toInt(ybts, 0), 2);
        Double bz = Convert.simpDouble(threebz + twobz + jbbz + ybbz, 2);
        //bz =  Convert.simpDouble(bz + Convert.toDouble(finSalRec.getFsjwf(), 0.0, 2), 2);
        finSalRec.setBzbt(bz.toString());
        //应发合计
        Double yfhj = Convert.simpDouble(yfxj + wybt2 + bz + Convert.toDouble(finSalRec.getFsjwf(), 0.0, 2) + Convert.toDouble(finSalRec.getQnf(), 0.0, 2), 2);
        finSalRec.setYfhj(yfhj.toString());
        finSalRec.setShxts(shxts);
        finSalRec.setSxts(sxts);
        finSalRec.setGzrys(jbts);
        finSalRec.setYbts(ybts);
        finSalRec.setSfhy(sfhy);

        //扣发项目
        String dkylbx = "";    //代扣养老保险
        String dkyilbx = ""; //代扣医疗保险
        String dksybx = ""; //代扣失业保险
        String dkgjj = ""; //代扣公积金
        String dkqynj = ""; //代扣企业年金
        String bkyl = ""; //补扣养老
        String bkyil = ""; //补扣医疗
        String bksy = ""; //补扣失业
        String bkgjj = ""; //补扣公积金
        String bkqynj = ""; //补扣企业年金
        String dksf = ""; //代扣水费
        String dkglwsf = ""; //代扣管理卫生费
        String kgsdjsb = ""; //扣公司代缴社保
        String bydkgrsds = ""; //本月代扣个人所得税
        String kqkkhj = ""; //考勤扣款合计
        String kkhj = ""; //扣款合计
        if (deducts != null && deducts.size() > 0) {
            FinDeduct deduct = deducts.get(0);
            FinDeductDetail detail = new FinDeductDetail();
            detail.setPid(deduct.getId());
            detail.setUserid(userid);
            List<FinDeductDetail> details = finDeductDetailMapper.selectFinDeductDetailList(detail);
            if (details.size() > 0) {
                detail = details.get(details.size() - 1);
                dkylbx = detail.getDkylbx();    //代扣养老保险
                dkyilbx = detail.getDkyilbx(); //代扣医疗保险
                dksybx = detail.getDksybx(); //代扣失业保险
                dkgjj = detail.getDkgjj(); //代扣公积金
                dkqynj = detail.getDkqynj(); //代扣企业年金
                bkyl = detail.getBkyl(); //补扣养老
                bkyil = detail.getBkyil(); //补扣医疗
                bksy = detail.getBksy(); //补扣失业
                bkgjj = detail.getBkgjj(); //补扣公积金
                bkqynj = detail.getBkqynj(); //补扣企业年金
                dksf = detail.getDksf(); //代扣水费
                dkglwsf = detail.getDkglwsf(); //代扣管理卫生费
                kgsdjsb = detail.getKgsdjsb(); //扣公司代缴社保
                bydkgrsds = detail.getBydkgrsds(); //本月代扣个人所得税
                kqkkhj = detail.getKqkkhj(); //考勤扣款合计
                kkhj = detail.getKkhj(); //扣款合计
            }
        }
        finSalRec.setDkylbx(dkylbx);
        finSalRec.setDkyibx(dkyilbx);
        finSalRec.setDksybx(dksybx);
        finSalRec.setDkgjj(dkgjj);
        finSalRec.setDkqynj(dkqynj);
        finSalRec.setBkyl(bkyl);
        finSalRec.setBkyil(bkyil);
        finSalRec.setBksy(bksy);
        finSalRec.setBkgjj(bkgjj);
        finSalRec.setBkqynj(bkqynj);
        finSalRec.setDksf(dksf);
        finSalRec.setDkglwsf(dkglwsf);
        finSalRec.setKgsdjsb(kgsdjsb);
        finSalRec.setDksds(bydkgrsds);
        finSalRec.setKqkk(kqkkhj);
        finSalRec.setKkhj(kkhj);
            //已在扣款统计导入中计算完成
        Double dkylbx2 = Convert.toDouble(dkylbx, 0.0, 2);	//代扣养老保险
        Double dkyilbx2 = Convert.toDouble(dkyilbx, 0.0, 2); //代扣医疗保险
        Double dksybx2 = Convert.toDouble(dksybx, 0.0, 2); //代扣失业保险
        Double dkgjj2 = Convert.toDouble(dkgjj, 0.0, 2); //代扣公积金
        Double dkqynj2 = Convert.toDouble(dkqynj, 0.0, 2); //代扣企业年金
        Double bkyl2 = Convert.toDouble(bkyl, 0.0, 2); //补扣养老
        Double bkyil2 = Convert.toDouble(bkyil, 0.0, 2); //补扣医疗
        Double bksy2 = Convert.toDouble(bksy, 0.0, 2); //补扣失业
        Double bkgjj2 = Convert.toDouble(bkgjj, 0.0, 2); //补扣公积金
        Double bkqynj2 = Convert.toDouble(bkqynj, 0.0, 2); //补扣企业年金
        Double dksf2 = Convert.toDouble(dksf, 0.0, 2); //代扣水费
        Double dkglwsf2 = Convert.toDouble(dkglwsf, 0.0, 2); //代扣管理卫生费
        Double kgsdjsb2 = Convert.toDouble(kgsdjsb, 0.0, 2); //扣公司代缴社保
        Double bydkgrsds2 = Convert.toDouble(bydkgrsds, 0.0, 2); //本月代扣个人所得税
        Double kqkkhj2 = Convert.toDouble(kqkkhj, 0.0, 2); //考勤扣款合计
        Double kkhj2 = Convert.simpDouble(dkylbx2+dkyilbx2+dksybx2+dkgjj2+dkqynj2+bkyl2+bkyil2+bksy2+bkgjj2+bkqynj2+dksf2+dkglwsf2+kgsdjsb2+bydkgrsds2+kqkkhj2, 2);
        finSalRec.setKkhj(kkhj2.toString());
        Double sfgz = Convert.simpDouble(yfhj - Convert.toDouble(kkhj, 0.0, 2), 2);
        finSalRec.setSfgz(sfgz.toString());
        finSalRec.setZwbt("");
        //职务补贴
        if ("4".equals(paramRank)){
            Double zwbt = 25000.0;
            finSalRec.setPostValue("");
            finSalRec.setNggz("");
            finSalRec.setYxqxed("");
            finSalRec.setZfbt("");
            finSalRec.setDzf("");
            finSalRec.setGwxy("");
            finSalRec.setZfbt("");
            finSalRec.setZfbtxy("");
            finSalRec.setGdjj("");
            finSalRec.setZwbt(zwbt.toString());
            finSalRec.setGrxyze("");
            finSalRec.setBmjljx("");
            finSalRec.setGrjljx("");
            finSalRec.setGrkfjx("");
            finSalRec.setSfxygz(zwbt.toString());
            finSalRec.setJxgz2("");
            finSalRec.setFsjwf("");
            finSalRec.setQnf("");
            finSalRec.setYfxj(zwbt.toString());
            finSalRec.setWybt("");
            finSalRec.setBzbt("");
            finSalRec.setKqdj("");
            finSalRec.setRankCode("");
            finSalRec.setBmjljx("");
            finSalRec.setKqcd("");
            finSalRec.setKqzt("");
            finSalRec.setYfhj(zwbt.toString());
            sfgz = Convert.simpDouble(zwbt-Convert.toDouble(kkhj,0.0,2),2);
            finSalRec.setSfgz(sfgz.toString());
        }

        return finSalRec;
    }

    //计算个人效益工资
    @Override
    public FinSalRec calcUserXygz(User user, FinSalRec finSalRec) {
        Long userid = user.getUserId();
        FinUserParam finUserParam = new FinUserParam();
        finUserParam.setUserId(userid);
        List<FinUserParam> finUserParams = finUserParamMapper.selectFinUserParamList(finUserParam);
        if (finUserParams.size() > 0) finUserParam = finUserParams.get(0);
        String nggz = finUserParam.getNggz()!=null?finUserParam.getNggz():"";
        String gwgz = finUserParam.getGwgz()!=null?finUserParam.getGwgz():"";

        finSalRec.setNggz(nggz);
        finSalRec.setPostValue(gwgz);

        Double gzxx2 = Convert.toDouble(finSalRec.getGzxs(), 0.0, 2); //工资系数
        Double nggz2 = Convert.toDouble(nggz, 0.0, 2); //年功工资
        Double gwgz2 = Convert.toDouble(gwgz, 0.0, 2); //岗位工资
        Double gdjj2 = Convert.toDouble(finSalRec.getGdjj(), 0.0, 2); //固定奖金

        //住房补贴
        Double zfbt = Convert.simpDouble((nggz2 + gwgz2) * 0.35, 2);
        finSalRec.setZfbt(zfbt.toString());
        //岗位效益
        Double gwxy = Convert.simpDouble(gwgz2 * gzxx2, 2);
        finSalRec.setGwxy(gwxy.toString());
        //住房补贴效益
        Double zfbtxy = Convert.simpDouble(gwxy * 0.35, 2);
        finSalRec.setZfbtxy(zfbtxy.toString());

        //个人效益工资总额
        Double grxyze = gwxy + zfbtxy + gdjj2;
        finSalRec.setGrxyze(grxyze.toString());

        return finSalRec;
    }

    //根据财务附件获取合计
    @Override
    public FinSal getFinSalOverview(FinSal finSal, HSSFWorkbook sheets) throws Exception {
        HSSFSheet sheet = sheets.getSheet("工资表1.0");
        int lastRowNum = sheet.getLastRowNum();
        String heji = "";
        String jiansheheji = "";
        String xinxiheji = "";
        HSSFFormulaEvaluator evaluator = sheets.getCreationHelper().createFormulaEvaluator();
        if (lastRowNum > 3) {
            for (int i=lastRowNum; i>60; i--){
                HSSFRow row = sheet.getRow(i);
                HSSFCell cellHj = row.getCell(0);
                if (cellHj != null){
                    String cellHjString = cellHj.toString();
                    if (cellHjString.replaceAll(" ", "").equals("合计")){
                        heji += getOverviweString(cellHjString, row, evaluator, 30, 48)+"\n";
                    }
                    if (cellHjString.replaceAll(" ", "").equals("建设公司合计")){
                        jiansheheji += "其中，建设公司："+getOverviweString(cellHjString, row, evaluator, 30, 48)+"\n";
                    }
                    if (cellHjString.replaceAll(" ", "").equals("信息公司合计")){
                        xinxiheji += "信息公司："+getOverviweString(cellHjString, row, evaluator, 30, 48);
                    }
                }

            }
        }
        finSal.setContent(heji+jiansheheji+xinxiheji);
        return finSal;
    }

    //合计文本生成
    private String getOverviweString(String prefix, HSSFRow row, HSSFFormulaEvaluator evaluator, int first, int second){
        String result = "";
        if (prefix != null && prefix.replaceAll(" ", "").equals(prefix)){
            HSSFCell cellYf = row.getCell(first);
            CellValue cellYfValue = evaluator.evaluate(cellYf);
            Double cellYfStr = Convert.simpDouble(cellYfValue.getNumberValue(), 2);
            HSSFCell cellSf = row.getCell(second);
            CellValue cellSfValue = evaluator.evaluate(cellSf);
            Double cellSfStr = Convert.simpDouble(cellSfValue.getNumberValue(), 2);
            result = "应发合计"+cellYfStr+"元，"+"实发合计"+cellSfStr+"元";
        }
        return result;
    }

    //根据数据计算合计
    @Override
    public List<FinSalRec> calcFinSalOverview(FinSal finSal, List<FinSalRec> finSalRecs) {
        //合计
        FinSalRec hjRec = new FinSalRec();
        FinSalRec jsRec = new FinSalRec();
        FinSalRec xxRec = new FinSalRec();

        for (int i=0; i<finSalRecs.size(); i++){
            FinSalRec finSalRec = finSalRecs.get(i);
            hjRec = eachSalRecItemAdd(hjRec, finSalRec, "合计");
            if (i>=finSalRecs.size()-6 && i<finSalRecs.size()){
                jsRec = eachSalRecItemAdd(jsRec, finSalRec, "建设公司合计");
            }
            if (i<finSalRecs.size()-6){
                xxRec = eachSalRecItemAdd(xxRec, finSalRec, "信息公司合计");
            }
        }
        finSalRecs.add(hjRec);
        finSalRecs.add(jsRec);
        finSalRecs.add(xxRec);
        return finSalRecs;
    }

    //合计计算方法
    private FinSalRec eachSalRecItemAdd(FinSalRec hjSalRec, FinSalRec eachSalRec, String title){
        hjSalRec.setUserid(-1L);
        hjSalRec.setUserName(title);
        hjSalRec.setDeptid(-1L);
        hjSalRec.setDeptName("");
        hjSalRec.setRankCode("");
        hjSalRec.setRankText(null);
        hjSalRec.setRankValue(null);
        hjSalRec.setPostCode(null);
        hjSalRec.setPostText(null);

        hjSalRec.setSfhy("");
        hjSalRec.setCreateBy(eachSalRec.getCreateBy());
        hjSalRec.setCreateTime(new Date());
        hjSalRec.setSalid(eachSalRec.getSalid());
        hjSalRec.setTimetag(eachSalRec.getTimetag());
        hjSalRec.setKqdj("");
        hjSalRec.setKqcd("");
        hjSalRec.setKqzt("");

        if (eachSalRec.getPostValue() != null) hjSalRec.setPostValue(Convert.simpDouble(Convert.toDouble(hjSalRec.getPostValue(), 0.0, 2)+Convert.toDouble(eachSalRec.getPostValue(), 0.0, 2),2).toString());
        if (eachSalRec.getNggz() != null) hjSalRec.setNggz(Convert.simpDouble(Convert.toDouble(hjSalRec.getNggz(), 0.0, 2)+Convert.toDouble(eachSalRec.getNggz(), 0.0, 2),2).toString());
        if (eachSalRec.getYxqxed() != null) hjSalRec.setYxqxed(Convert.simpDouble(Convert.toDouble(hjSalRec.getYxqxed(), 0.0, 2)+Convert.toDouble(eachSalRec.getYxqxed(), 0.0, 2),2).toString());
        if (eachSalRec.getZfbt() != null) hjSalRec.setZfbt(Convert.simpDouble(Convert.toDouble(hjSalRec.getZfbt(), 0.0, 2)+Convert.toDouble(eachSalRec.getZfbt(), 0.0, 2),2).toString());
        if (eachSalRec.getDzf() != null) hjSalRec.setDzf(Convert.simpDouble(Convert.toDouble(hjSalRec.getDzf(), 0.0, 2)+Convert.toDouble(eachSalRec.getDzf(), 0.0, 2),2).toString());
        if (eachSalRec.getGwxy() != null) hjSalRec.setGwxy(Convert.simpDouble(Convert.toDouble(hjSalRec.getGwxy(), 0.0, 2)+Convert.toDouble(eachSalRec.getGwxy(), 0.0, 2),2).toString());
        if (eachSalRec.getZfbtxy() != null) hjSalRec.setZfbtxy(Convert.simpDouble(Convert.toDouble(hjSalRec.getZfbtxy(), 0.0, 2)+Convert.toDouble(eachSalRec.getZfbtxy(), 0.0, 2),2).toString());
        if (eachSalRec.getGdjj() != null) hjSalRec.setGdjj(Convert.simpDouble(Convert.toDouble(hjSalRec.getGdjj(), 0.0, 2)+Convert.toDouble(eachSalRec.getGdjj(), 0.0, 2),2).toString());

        if (eachSalRec.getZwbt() != null) hjSalRec.setZwbt(Convert.simpDouble(Convert.toDouble(hjSalRec.getZwbt(), 0.0, 2)+Convert.toDouble(eachSalRec.getZwbt(), 0.0, 2),2).toString());
        if (eachSalRec.getGrxyze() != null) hjSalRec.setGrxyze(Convert.simpDouble(Convert.toDouble(hjSalRec.getGrxyze(), 0.0, 2)+Convert.toDouble(eachSalRec.getGrxyze(), 0.0, 2),2).toString());

        if (eachSalRec.getBmjljx() != null) hjSalRec.setBmjljx(Convert.simpDouble(Convert.toDouble(hjSalRec.getBmjljx(), 0.0, 2)+Convert.toDouble(eachSalRec.getBmjljx(), 0.0, 2),2).toString());
        if (eachSalRec.getGrjljx() != null) hjSalRec.setGrjljx(Convert.simpDouble(Convert.toDouble(hjSalRec.getGrjljx(), 0.0, 2)+Convert.toDouble(eachSalRec.getGrjljx(), 0.0, 2),2).toString());
        if (eachSalRec.getGrkfjx() != null) hjSalRec.setGrkfjx(Convert.simpDouble(Convert.toDouble(hjSalRec.getGrkfjx(), 0.0, 2)+Convert.toDouble(eachSalRec.getGrkfjx(), 0.0, 2),2).toString());
        if (eachSalRec.getSfxygz() != null) hjSalRec.setSfxygz(Convert.simpDouble(Convert.toDouble(hjSalRec.getSfxygz(), 0.0, 2)+Convert.toDouble(eachSalRec.getSfxygz(), 0.0, 2),2).toString());
        if (eachSalRec.getJxgz2() != null) hjSalRec.setJxgz2(Convert.simpDouble(Convert.toDouble(hjSalRec.getJxgz2(), 0.0, 2)+Convert.toDouble(eachSalRec.getJxgz2(), 0.0, 2),2).toString());
        if (eachSalRec.getYfxj() != null) hjSalRec.setYfxj(Convert.simpDouble(Convert.toDouble(hjSalRec.getYfxj(), 0.0, 2)+Convert.toDouble(eachSalRec.getYfxj(), 0.0, 2),2).toString());

        if (eachSalRec.getWybt() != null) hjSalRec.setWybt(Convert.simpDouble(Convert.toDouble(hjSalRec.getWybt(), 0.0, 2)+Convert.toDouble(eachSalRec.getWybt(), 0.0, 2),2).toString());
        if (eachSalRec.getBf() != null) hjSalRec.setBf(Convert.simpDouble(Convert.toDouble(hjSalRec.getBf(), 0.0, 2)+Convert.toDouble(eachSalRec.getBf(), 0.0, 2),2).toString());
        if (eachSalRec.getBzbt() != null) hjSalRec.setBzbt(Convert.simpDouble(Convert.toDouble(hjSalRec.getBzbt(), 0.0, 2)+Convert.toDouble(eachSalRec.getBzbt(), 0.0, 2),2).toString());
        if (eachSalRec.getWptxbz() != null) hjSalRec.setWptxbz(Convert.simpDouble(Convert.toDouble(hjSalRec.getWptxbz(), 0.0, 2)+Convert.toDouble(eachSalRec.getWptxbz(), 0.0, 2),2).toString());
        if (eachSalRec.getGsjl() != null) hjSalRec.setGsjl(Convert.simpDouble(Convert.toDouble(hjSalRec.getGsjl(), 0.0, 2)+Convert.toDouble(eachSalRec.getGsjl(), 0.0, 2),2).toString());
        if (eachSalRec.getQnf() != null) hjSalRec.setQnf(Convert.simpDouble(Convert.toDouble(hjSalRec.getQnf(), 0.0, 2)+Convert.toDouble(eachSalRec.getQnf(), 0.0, 2),2).toString());

        if (eachSalRec.getYfhj() != null) hjSalRec.setYfhj(Convert.simpDouble(Convert.toDouble(hjSalRec.getYfhj(), 0.0, 2)+Convert.toDouble(eachSalRec.getYfhj(), 0.0, 2),2).toString());

        //扣
        if (eachSalRec.getDkylbx() != null) hjSalRec.setDkylbx(Convert.simpDouble(Convert.toDouble(hjSalRec.getDkylbx(), 0.0, 2)+Convert.toDouble(eachSalRec.getDkylbx(), 0.0, 2),2).toString());
        if (eachSalRec.getDkyibx() != null) hjSalRec.setDkyibx(Convert.simpDouble(Convert.toDouble(hjSalRec.getDkyibx(), 0.0, 2)+Convert.toDouble(eachSalRec.getDkyibx(), 0.0, 2),2).toString());
        if (eachSalRec.getDkgjj() != null) hjSalRec.setDkgjj(Convert.simpDouble(Convert.toDouble(hjSalRec.getDkgjj(), 0.0, 2)+Convert.toDouble(eachSalRec.getDkgjj(), 0.0, 2),2).toString());
        if (eachSalRec.getDksds() != null) hjSalRec.setDksds(Convert.simpDouble(Convert.toDouble(hjSalRec.getDksds(), 0.0, 2)+Convert.toDouble(eachSalRec.getDksds(), 0.0, 2),2).toString());
        if (eachSalRec.getKqkk() != null) hjSalRec.setKqkk(Convert.simpDouble(Convert.toDouble(hjSalRec.getKqkk(), 0.0, 2)+Convert.toDouble(eachSalRec.getKqkk(), 0.0, 2),2).toString());
        if (eachSalRec.getKkhj() != null) hjSalRec.setKkhj(Convert.simpDouble(Convert.toDouble(hjSalRec.getKkhj(), 0.0, 2)+Convert.toDouble(eachSalRec.getKkhj(), 0.0, 2),2).toString());

        //实发
        if (eachSalRec.getSfgzze() != null) hjSalRec.setSfgzze(Convert.simpDouble(Convert.toDouble(hjSalRec.getSfgzze(), 0.0, 2)+Convert.toDouble(eachSalRec.getSfgzze(), 0.0, 2),2).toString());
        if (eachSalRec.getSfgz() != null) hjSalRec.setSfgz(Convert.simpDouble(Convert.toDouble(hjSalRec.getSfgz(), 0.0, 2)+Convert.toDouble(eachSalRec.getSfgz(), 0.0, 2),2).toString());

        if (eachSalRec.getGzrys() != null) hjSalRec.setGzrys(Convert.simpDouble(Convert.toDouble(hjSalRec.getGzrys(), 0.0, 2)+Convert.toDouble(eachSalRec.getGzrys(), 0.0, 2),2).toString());
        if (eachSalRec.getSxts() != null) hjSalRec.setSxts(Convert.simpDouble(Convert.toDouble(hjSalRec.getSxts(), 0.0, 2)+Convert.toDouble(eachSalRec.getSxts(), 0.0, 2),2).toString());
        if (eachSalRec.getShxts() != null) hjSalRec.setShxts(Convert.simpDouble(Convert.toDouble(hjSalRec.getShxts(), 0.0, 2)+Convert.toDouble(eachSalRec.getShxts(), 0.0, 2),2).toString());
        if (eachSalRec.getYbts() != null) hjSalRec.setYbts(Convert.simpDouble(Convert.toDouble(hjSalRec.getYbts(), 0.0, 2)+Convert.toDouble(eachSalRec.getYbts(), 0.0, 2),2).toString());
        if (eachSalRec.getFsjwf() != null) hjSalRec.setFsjwf(Convert.simpDouble(Convert.toDouble(hjSalRec.getFsjwf(), 0.0, 2)+Convert.toDouble(eachSalRec.getFsjwf(), 0.0, 2),2).toString());

        if (eachSalRec.getDksybx() != null) hjSalRec.setDksybx(Convert.simpDouble(Convert.toDouble(hjSalRec.getDksybx(), 0.0, 2)+Convert.toDouble(eachSalRec.getDksybx(), 0.0, 2),2).toString());
        if (eachSalRec.getDkqynj() != null) hjSalRec.setDkqynj(Convert.simpDouble(Convert.toDouble(hjSalRec.getDkqynj(), 0.0, 2)+Convert.toDouble(eachSalRec.getDkqynj(), 0.0, 2),2).toString());
        if (eachSalRec.getBkyl() != null) hjSalRec.setBkyl(Convert.simpDouble(Convert.toDouble(hjSalRec.getBkyl(), 0.0, 2)+Convert.toDouble(eachSalRec.getBkyl(), 0.0, 2),2).toString());
        if (eachSalRec.getBkyil() != null) hjSalRec.setBkyil(Convert.simpDouble(Convert.toDouble(hjSalRec.getBkyil(), 0.0, 2)+Convert.toDouble(eachSalRec.getBkyil(), 0.0, 2),2).toString());
        if (eachSalRec.getBksy() != null) hjSalRec.setBksy(Convert.simpDouble(Convert.toDouble(hjSalRec.getBksy(), 0.0, 2)+Convert.toDouble(eachSalRec.getBksy(), 0.0, 2),2).toString());
        if (eachSalRec.getBkgjj() != null) hjSalRec.setBkgjj(Convert.simpDouble(Convert.toDouble(hjSalRec.getBkgjj(), 0.0, 2)+Convert.toDouble(eachSalRec.getBkgjj(), 0.0, 2),2).toString());
        if (eachSalRec.getBkqynj() != null) hjSalRec.setBkqynj(Convert.simpDouble(Convert.toDouble(hjSalRec.getBkqynj(), 0.0, 2)+Convert.toDouble(eachSalRec.getBkqynj(), 0.0, 2),2).toString());
        if (eachSalRec.getDksf() != null) hjSalRec.setDksf(Convert.simpDouble(Convert.toDouble(hjSalRec.getDksf(), 0.0, 2)+Convert.toDouble(eachSalRec.getDksf(), 0.0, 2),2).toString());
        if (eachSalRec.getDkglwsf() != null) hjSalRec.setDkglwsf(Convert.simpDouble(Convert.toDouble(hjSalRec.getDkglwsf(), 0.0, 2)+Convert.toDouble(eachSalRec.getDkglwsf(), 0.0, 2),2).toString());

        if (eachSalRec.getKgsdjsb() != null) hjSalRec.setKgsdjsb(Convert.simpDouble(Convert.toDouble(hjSalRec.getKgsdjsb(), 0.0, 2)+Convert.toDouble(eachSalRec.getKgsdjsb(), 0.0, 2),2).toString());

        hjSalRec.setIdcard("");
        hjSalRec.setYhkh("");

        return hjSalRec;
    }

    public void writeForwardExcel(Workbook wb, ModelMap mmap) {
        //生成sheet
        Sheet sheet = wb.createSheet("结转");
        //创建行
        Row row = null;

        //创建列
        Cell cell = null;

        //创建表头单元格样式
        CellStyle cs_header = wb.createCellStyle();
        //设置字体样式
        Font boldFont = wb.createFont();

        //设置文字类型
        boldFont.setFontName("宋体");
        //设置加粗
        boldFont.setBold(true);
        //设置文字大小
        boldFont.setFontHeightInPoints((short) 16);
        //应用设置的字体
        cs_header.setFont(boldFont);

        //设置边框下、左、右、上
        cs_header.setBorderBottom(BorderStyle.THIN);
        cs_header.setBorderLeft(BorderStyle.THIN);
        cs_header.setBorderRight(BorderStyle.THIN);
        cs_header.setBorderTop(BorderStyle.THIN);
        //水平居中
        cs_header.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cs_header.setVerticalAlignment(VerticalAlignment.CENTER);
        //前景填充色
//        cs_header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);

        //设置前景填充样式
//        cs_header.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //创建文本单元格样式
        CellStyle cs_text = wb.createCellStyle();
        //创建文字设置
        Font textFont = wb.createFont();
        //设置文字类型
        textFont.setFontName("宋体");//Consolas
        //设置文字大小
        textFont.setFontHeightInPoints((short) 10);
        // 应用设置
        cs_text.setFont(textFont);
        //启用文本换行
        cs_text.setWrapText(true);
        //设置边框
        cs_text.setBorderBottom(BorderStyle.THIN);
        cs_text.setBorderLeft(BorderStyle.THIN);
        cs_text.setBorderRight(BorderStyle.THIN);
        cs_text.setBorderTop(BorderStyle.THIN);
        //水平居中
        cs_text.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中
        cs_text.setVerticalAlignment(VerticalAlignment.CENTER);

        //1、设置标题行1
        row = sheet.createRow(0);
        //设置单元格行高
        row.setHeightInPoints(24);
        //创建单元格
        cell = row.createCell(0);
        //设置单元格内容
        String xlsName = "结转";
        cell.setCellValue(xlsName);
        //设置单元格样式
        cell.setCellStyle(cs_header);

        List<FinSalRec> finSalRecs = (List<FinSalRec>) mmap.get("finSalRecs");

        //创建表格
        for (int i=1; i<=24; i++){
            //1、设置标题行1
            row = sheet.createRow(i);
            //设置单元格行高
            row.setHeightInPoints(20);
            for (int j=0; j<9; j++){
                //创建单元格
                cell = row.createCell(j);
                //设置单元格样式
                cell.setCellStyle(cs_text);
            }
        }
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, 0, 0, 8));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1, 12, 0, 0));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1, 12, 3, 3));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(1, 12, 6, 6));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(13, 24, 0, 0));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(13, 24, 3, 3));
        sheet.addMergedRegionUnsafe(new CellRangeAddress(13, 24, 6, 6));

        setSheetValue(1, 0, finSalRecs, "工资", "sfgz", sheet);
        setSheetValue(1, 3, finSalRecs, "住房补贴", "zfbt", sheet);
        setSheetValue(1, 6, finSalRecs, "独子费", "dzf", sheet);
        setSheetValue(13, 0, finSalRecs, "物业补贴", "wybt", sheet);

    }

    private void setSheetValue(int startRow, int startCol, List<FinSalRec> finSalRecs, String tableName, String colName, Sheet sheet) {
        sheet.getRow(startRow).getCell(startCol).setCellValue(tableName);
        for (int i=0; i<finSalRecs.size(); i++){
            FinSalRec finSalRec = finSalRecs.get(i);
            sheet.getRow(startRow+i).getCell(startCol+1).setCellValue(finSalRec.getDeptName());
            try {
                String colValue = BeanUtils.getProperty(finSalRec, colName);
                sheet.getRow(startRow+i).getCell(startCol+2).setCellValue(colValue);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<FinSalRec> selectForwardFinItems(FinSalRec finSalRec) {
        List<FinSalRec> finSalRecs = finSalRecMapper.selectForwardFinItems(finSalRec);
        for (int i=0; i<finSalRecs.size(); i++){
            FinSalRec innerItem = finSalRecs.get(i);
            if (innerItem.getDeptName().indexOf("山东机场") >= 0){
                innerItem.setDeptName("公司领导");
                break;
            }
        }
        return finSalRecs;
    }

    @Override
    public List<FinSalRec> selectForwardFinDeducts(FinSalRec finSalRec) {
        List<FinSalRec> finSalRecs = finSalRecMapper.selectForwardFinDeducts(finSalRec);
        return finSalRecs;
    }

    @Override
    public List<FinSalRec> selectForwardFinCounts(FinSalRec finSalRec) {
        List<FinSalRec> finSalRecs = finSalRecMapper.selectForwardFinCounts(finSalRec);
        return finSalRecs;
    }

    @Override
    public List<Integer> selectSpecificUserids(FinSalRec finSalRec) {
        List<Integer> userids = finSalRecMapper.selectSpecificUserids(finSalRec);
        return userids;
    }

    @Override
    public void writeFinSalWord(OutputStream os, ModelMap mmap) throws IOException {
        XWPFDocument document = new XWPFDocument();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        FinSal finSal = (FinSal) mmap.get("finSal");
        List<RecLine> recLines = (List<RecLine>) mmap.get("recLines");
        List<RecLine> conRecLines = (List<RecLine>) mmap.get("conRecLines");
        String[] flowNodeArr = (String[]) mmap.get("flowNodeArr");

        //标题
        XWPFParagraph p = document.createParagraph();
        p.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = p.createRun();
        run.setText(finSal.getDeptName() + " " + finSal.getSalname());
        run.setBold(true);
        run.setFontSize(16);
        run.setColor("FF0000");
        run.addBreak();
        //基本信息
        String createTime = df.format(finSal.getCreateTime());

        XWPFParagraph p2 = document.createParagraph();
        p2.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun run2 = p.createRun();
        run2.setText("日期：" + createTime + "    流水号：XXGS_GZSP-" + finSal.getTimetag() + "-" + finSal.getId());
        run2.setFontSize(12);

        //表格
        XWPFTable table = document.createTable(2+flowNodeArr.length, 2);
        table.setWidth("100%");

        // 应用样式到所有单元格
        for (XWPFTableRow row : table.getRows()) {
            /*CTRow ctRow = row.getCtRow();
            CTHeight ctHeight = ctRow.getTrPr().addNewTrHeight();
            ctHeight.setVal(BigInteger.valueOf(90));*/

            for (XWPFTableCell cell : row.getTableCells()) {
                CTTcPr tcPr = cell.getCTTc().getTcPr();
                if (tcPr == null) {
                    tcPr = cell.getCTTc().addNewTcPr();
                }
                if (tcPr.getTcBorders() == null) {
                    tcPr.addNewTcBorders();
                }
                CTBorder border = tcPr.getTcBorders().addNewTop(); // 可以是 addNewBottom, addNewLeft, addNewRight, addNewInsideH, addNewInsideV 等
                setDocBorderStyle(border);
                CTBorder border2 = tcPr.getTcBorders().addNewBottom(); // 可以是 addNewBottom, addNewLeft, addNewRight, addNewInsideH, addNewInsideV 等
                setDocBorderStyle(border2);
                CTBorder border3 = tcPr.getTcBorders().addNewLeft(); // 可以是 addNewBottom, addNewLeft, addNewRight, addNewInsideH, addNewInsideV 等
                setDocBorderStyle(border3);
                CTBorder border4 = tcPr.getTcBorders().addNewRight(); // 可以是 addNewBottom, addNewLeft, addNewRight, addNewInsideH, addNewInsideV 等
                setDocBorderStyle(border4);
            }
        }

        XWPFTableRow headerRow = table.getRow(0);
        headerRow.getCell(0).setText("工资信息清单");
        XWPFRun innerRun = headerRow.getCell(1).getParagraphs().get(0).createRun();
        innerRun.setText(finSal.getContent());

        XWPFTableRow firstRow = table.getRow(1);
        firstRow.getCell(0).setText("制表人");
        firstRow.getCell(1).setText(recLines.get(0).getCreateName() + " " + df.format(recLines.get(0).getCreateTime()) + " 流程发起");

        //数据
        for (int i = 0; i < flowNodeArr.length; i++) {
            XWPFTableRow row = table.getRow(i + 2);
            String roleName = "";
            if (flowNodeArr[i].indexOf("D") >= 0) {
                roleName = flowNodeArr[i].substring(flowNodeArr[i].indexOf("D")+1, flowNodeArr[i].indexOf("P"));
            } else {
                roleName = flowNodeArr[i].substring(flowNodeArr[i].indexOf("P")+1);
            }
            if ("财务管理部".equals(roleName)){
                roleName = "财务总监";
            }
            if ("山东机场信息公司".equals(roleName)){
                roleName = "董事长";
            }

            row.getCell(0).setText(roleName); // 假设deptName存储角色信息
            for (int j=0; j<recLines.size(); j++){
                RecLine item = recLines.get(j);
                if (item.getLineIndex() == i && "1".equals(item.getOperateStatus())){
                    row.getCell(1).setText(item.getOperateName() + " " + df.format(item.getOperateTime()) + " 同意"); // 假设userName存储人员信息
                    break;
                }
            }
        }

        // 设置列宽
        table.getRows().get(0).getCell(0).getCTTc().addNewTcPr().addNewTcW().setW(new BigInteger("3000")); // 第一列宽度
        table.getRows().get(0).getCell(1).getCTTc().addNewTcPr().addNewTcW().setW(new BigInteger("7000")); // 第二列宽度

        document.write(os);
        document.close();
    }

    private void setDocBorderStyle(CTBorder border){
        border.setVal(STBorder.SINGLE); // 设置边框类型为单线
        border.setColor("FF0000"); // 设置颜色，这里是红色（FF0000）的RGB值
        border.setSz(new BigInteger("12")); // 设置边框粗细，这里是4pt
        border.setSpace(new BigInteger("8")); // 设置边框间距，这里是0pt
    }

}

