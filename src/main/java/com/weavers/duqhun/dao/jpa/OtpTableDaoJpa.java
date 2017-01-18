/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weavers.duqhun.dao.jpa;

import com.weavers.duqhun.dao.OtpTableDao;
import com.weavers.duqhun.domain.OtpTable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Android-3
 */
public class OtpTableDaoJpa extends BaseDaoJpa<OtpTable> implements OtpTableDao {

    public OtpTableDaoJpa() {
        super(OtpTable.class, "OtpTable");
    }

    @Override
    public boolean isValidOtp(Long userId, String email, String otp) {  // return true if OTP is valide else return false.
        try {
            Query query = getEntityManager().createQuery("SELECT otp FROM OtpTable otp WHERE otp.otp=:otp AND otp.userMail=:email AND otp.userId=:userId AND otp.sendTime>:time");
            query.setParameter("email", email);
            query.setParameter("userId", userId);
            query.setParameter("otp", otp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, -10);
            query.setParameter("time", cal.getTime());
            Object obj = query.getSingleResult();
            return true;
        } catch (NoResultException nre) {
            return false;
        }
    }

    @Override
    public OtpTable getValidOtp(Long userId, String email, String otp) {    // return OTP if it is valide
        try {
            Query query = getEntityManager().createQuery("SELECT otp FROM OtpTable otp WHERE otp.otp=:otp AND otp.userMail=:email AND otp.userId=:userId AND otp.sendTime>:time");
            query.setParameter("email", email);
            query.setParameter("userId", userId);
            query.setParameter("otp", otp);
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MINUTE, -10);
            query.setParameter("time", cal.getTime());
            return (OtpTable) query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
