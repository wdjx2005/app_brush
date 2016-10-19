package com.eazy.brush.service.impl;

import com.eazy.brush.core.utils.HttpUtil;
import com.eazy.brush.core.utils.NiuMaUtil;
import com.eazy.brush.service.PhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by yuekuapp on 16-10-18.
 */
@Service
@Slf4j
public class PhoneNumberServiceImpl implements PhoneNumberService {

//    @Value("${niuma.uid}")
    private String username="xuguang91915";

//    @Value("${niuma.pwd}")
    private String password="xuguang";

    private String token;


    @PostConstruct
    @Override
    public void loginIn() {
        if(StringUtils.isEmpty(token)){
            log.info("begin login niuma!");
            String result = HttpUtil.get(NiuMaUtil.getLogin(username,password));
            log.info("login result:"+result);
            if(result.contains(username)){
                token=result.split("\\|")[1];
            }else{
                log.info("niu ma login error!"+result);
            }
        }
    }

    @Override
    public String getMobileNum(String pid, int size) {
        loginIn();
        releaseMobileNum("");//获取新的号码之前,首先清空旧的号码
        String result="";
        while(!result.contains(token)){
            result=HttpUtil.get(NiuMaUtil.getMobileNum(pid,username,token));
            log.info("niuma getmobile num :"+result);
        }
        return result.split("\\|")[0];
    }

    @Override
    public void releaseMobileNum(String mobile) {
        loginIn();
        HttpUtil.get(NiuMaUtil.releaseMobileNum(username,token,mobile));
    }

    @Override
    public String getMobileCodeAndRelease(String pid, String mobile) {
        loginIn();
        String result= HttpUtil.get(NiuMaUtil.getVcodeAndReleaseMobile(username,token,pid,mobile));
        if(result.contains(mobile)){
            return result.split("|")[1];
        }
        log.info("get mobile code error :"+result);
        return "";
    }

    @Override
    public void addIgnoreList(String mobiles, String pid) {
        loginIn();
        HttpUtil.get(NiuMaUtil.addIgnoreList(username,token,mobiles,pid));
    }


}
