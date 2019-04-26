package json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.BeanUtils;
import 继承树.Super;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastJson循环引用 {

    public static void main(String[] args) {

//        List<A> list = new ArrayList<>();

        /*A a = new A(1, 1);
        list.add(a);
//        A ac = new A(2, 3);
//
//        BeanUtils.copyProperties(a, ac);

        list.add(a);*/

        //重复引用，一个对象中多个属性同时引用同一个对象

          /*
          //重复引用，一个对象中多个属性同时引用同一个对象
          A a = new A(1, 1);
          list.add(a);
          list.add(a);
          System.out.println(JSON.toJSONString(list));

          打印：[{"anInt":1,"bAnInt":1},{"$ref":"$[0]"}]
          */


        /*
        循环引用，对象的属性之间存在相互引用导致循环，
        将会一直打印{"map2":{"map1":{{"map2":{"map1":...}}}，
        最终会引起StackOverFlow异常
        Map<String, Object> map1 = new HashMap<>();
        Map<String, Object> map2 = new HashMap<>();

        map1.put("map2", map2);
        map2.put("map1", map1);


        System.out.println(JSON.toJSONString(map1));
        打印：{"map2":{"map1":{"$ref":".."}}}
        */

        //在确定自己的操作是安全的重复引用之后，可以通过这种方式暂时关闭循环引用检测
//        System.out.println(JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));

        //规则
        /*
        语法	描述
{“$ref”:”\$”}	引用根对象
{“$ref”:”@”}	引用自己
{“$ref”:”..”}	引用父对象
{“$ref”:”../..”}	引用父对象的父对象
{“$ref”:”\$.members[0].reportTo”}	基于路径的引用
         */

    }

}
