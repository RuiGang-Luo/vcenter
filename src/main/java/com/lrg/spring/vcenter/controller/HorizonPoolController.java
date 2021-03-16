package com.lrg.spring.vcenter.controller;

import com.lrg.spring.vcenter.context.DataCache;
import com.lrg.spring.vcenter.pojo.ResultEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/horizon")
public class HorizonPoolController {
    private static final String DATA_CLASS="Pool.json";

    @GetMapping("/pool/list")
    public ResultEntity poolList() throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return new ResultEntity(200,null, DataCache.getDataList(DATA_CLASS));
    }
}
