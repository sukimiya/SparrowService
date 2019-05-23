package io.e2x.sparrow.nest.web.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.util.JSON;
import io.e2x.sparrow.nest.config.SparrowConfigurationRepository;
import io.e2x.sparrow.nest.gm.ItemTipRepository;
import io.e2x.sparrow.nest.gm.RewordRepository;
import io.e2x.sparrow.nest.gm.vo.DispatcherReword;
import io.e2x.sparrow.nest.gm.vo.ItemTipVO;
import io.e2x.sparrow.nest.gm.vo.SQLiteJSONDataVO;
import io.e2x.sparrow.nest.gm.vo.SQLiteJSONVO;
import io.e2x.sparrow.nest.security.model.OAuthClientRepository;
import io.e2x.sparrow.nest.security.model.OUserDetailRepository;
import io.e2x.sparrow.nest.users.UserInfoRepository;
import io.e2x.sparrow.nest.web.controller.event.ResultEventTypes;
import io.e2x.sparrow.nest.web.controller.event.SampleWebPageEvent;
import io.netty.util.concurrent.Promise;
import io.swagger.annotations.Api;
import org.bouncycastle.util.io.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import springfox.documentation.spring.web.json.Json;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Timestamp;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Api(value = "/gm",tags = "Game master Services")
@RestController
@RequestMapping(value = "gm")
public class GameRestController {
    @Autowired
    public SparrowConfigurationRepository s_config;

    @Value("${resource.ssn:spring-boot-application}")
    private String resourceId;

    private final int LIST_PAGE_SIZE=20;

    private RewordRepository rewordRepository;
    private ItemTipRepository itemTipRepository;

    public GameRestController(RewordRepository rewordRepository, ItemTipRepository itemTipRepository) {
        this.rewordRepository = rewordRepository;
        this.itemTipRepository = itemTipRepository;
    }

//    @PreAuthorize("hasAuthority('DISPATCHER')")
    @PostMapping("uploadItemTip")
    public ResultEventTypes puloadItemTip(@RequestParam(value = "file") MultipartFile file){
        try {
            ObjectMapper mapper = new ObjectMapper();
            itemTipRepository.deleteAll();
            String jdocStr = new String(file.getBytes(), StandardCharsets.UTF_8);
            List<SQLiteJSONVO> sqLiteJSONVOList = mapper.readValue(jdocStr, new TypeReference<List<SQLiteJSONVO>>(){});
            for(int i=0;i<sqLiteJSONVOList.size();i++){
                SQLiteJSONVO sqLiteJSONVO = sqLiteJSONVOList.get(i);
                for(int c =0;c<sqLiteJSONVO.data.size();c++){
                    ArrayList dataVO = sqLiteJSONVO.data.get(c);
                    Integer itemId = (Integer) dataVO.get(0);
                    String itemName = (String)dataVO.get(1);
                    String itemDesc = (String)dataVO.get(2);
                    ItemTipVO itemTipVO = new ItemTipVO(itemId,itemName,itemDesc);
                    itemTipRepository.save(itemTipVO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResultEventTypes.UNKWON_ERROR;
        }
        return ResultEventTypes.OK;
    }
    @GetMapping("items")
    public List<ItemTipVO> getAllItemTips(){
        return itemTipRepository.findAll();
    }
    @PostMapping("sendReword")
    public ResultEventTypes sendReword (@RequestParam(value = "ssn",required = true) String ssn){

        DispatcherReword dispatcherReword = rewordRepository.findBySsn(ssn);
        if(dispatcherReword == null){
            return  ResultEventTypes.UNKWON_ERROR.setReason("找不到相应的奖励");
        }
        String [] roles = dispatcherReword.role.split(",");
        String [] items = dispatcherReword.itemType.split(",");
        for(int r=0;r<roles.length;r++){
            for(int i=0;i<items.length;i++){
                String role = roles[r];
                String item = items[i];
                String url = "http://101.132.193.16:9307/reward?rolename=" + role + "&itemid=" + item + "&num=" + dispatcherReword.num.toString();
                sendOneReword(role, item, dispatcherReword.num).subscribe(s -> System.out.println(s));
            }
        }
        dispatcherReword.sent = true;
        rewordRepository.save(dispatcherReword);
        return ResultEventTypes.OK;
    }
    private Mono<String> sendOneReword(String role, String itemid, Integer num){
            String url = "http://101.132.193.16:9307/reward?rolename=" + role + "&itemid=" + itemid + "&num=" + Integer.toString(num);
            WebClient webClient = WebClient.create(url);
            return webClient.get().uri(url).retrieve().bodyToMono(String.class);
//            System.out.println("sendOneReword:"+url);
//            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
    }
    @PostMapping("createreword")
    public ResultEventTypes createReword(@RequestBody DispatcherReword reword){
        DispatcherReword newReword = new DispatcherReword(new Date().getTime(),reword.role==null?"":reword.role,reword.itemType,reword.itemName,reword.num);
        rewordRepository.save(newReword);
        return ResultEventTypes.OK;
    }
    @PostMapping("getrewords")
    public List<DispatcherReword> getRewords(@RequestBody SampleWebPageEvent pageEvent){
        List<DispatcherReword> dispatcherRewordList;
        Date startDate = new Date();
        Date endDate = new Date();
        Boolean isDig = false;
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if(pageEvent.key!=null)
            isDig =  pattern.matcher(pageEvent.key).matches();
        String itemType = "";
        if(isDig) itemType = pageEvent.key;
        if(pageEvent.time1!=null){
            if(pageEvent.time2!=null){
                startDate.setTime(Long.parseLong(pageEvent.time1));
                endDate.setTime(Long.parseLong(pageEvent.time1));
            }else{
                startDate.setTime(Long.parseLong(pageEvent.time1));
                endDate.setDate(endDate.getDate()+1);
            }
            if(pageEvent.key!=null){
                if(isDig)
                    dispatcherRewordList =  rewordRepository.findAllByTimestampBetweenAndItemTypeIsLike(startDate.getTime(),endDate.getTime(), itemType);
                else
                    dispatcherRewordList =  rewordRepository.findAllByTimestampBetweenAndRoleIsLike(startDate.getTime(),endDate.getTime(),pageEvent.key);
            }else{
                dispatcherRewordList =  rewordRepository.findAllByTimestampBetween(startDate.getTime(),endDate.getTime());
            }
        }else if(pageEvent.key!=null){
            if(isDig)
                dispatcherRewordList =  rewordRepository.findAllByItemTypeIsLike(itemType);
            else
                dispatcherRewordList =  rewordRepository.findAllByRoleIsLike(pageEvent.key);
        }else{
            dispatcherRewordList = rewordRepository.findAll();

        }
        Collections.reverse(dispatcherRewordList);
        if(pageEvent.page!=0) {
            int start = pageEvent.pageSize * pageEvent.page;
            int max = dispatcherRewordList.size();
            if(start>=max){
//                start = (max - pageEvent.pageSize)>0?(max - pageEvent.pageSize):0;
                dispatcherRewordList.clear();
                return dispatcherRewordList;
            }
            int end = start + pageEvent.pageSize;
            if(end>max) end = max;
            return dispatcherRewordList.subList(start,end);
        }else
            return dispatcherRewordList;
    }
}
