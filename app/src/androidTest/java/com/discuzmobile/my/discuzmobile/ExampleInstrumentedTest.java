package com.discuzmobile.my.discuzmobile;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.alibaba.fastjson.JSON;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.discuzmobile.my.discuzmobile", appContext.getPackageName());
    }

    public static void main(String[] args) {
        List<Kind> kinds = JSON.parseArray("[\n" +
                "        {\n" +
                "            \"kindId\": 1,\n" +
                "            \"kindName\": \"美食\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"kindId\": 2,\n" +
                "            \"kindName\": \"科技\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"kindId\": 3,\n" +
                "            \"kindName\": \"旅游\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"kindId\": 4,\n" +
                "            \"kindName\": \"时尚\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"kindId\": 5,\n" +
                "            \"kindName\": \"生活\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"kindId\": 6,\n" +
                "            \"kindName\": \"娱乐\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"kindId\": 7,\n" +
                "            \"kindName\": \"新闻\"\n" +
                "        }\n" +
                "    ]", Kind.class);

        System.out.println(kinds.size());
    }

    class Kind {
        private Long kindId;
        private String kindName;

        public Long getKindId() {
            return kindId;
        }

        public void setKindId(Long kindId) {
            this.kindId = kindId;
        }

        public String getKindName() {
            return kindName;
        }

        public void setKindName(String kindName) {
            this.kindName = kindName;
        }
    }
}
