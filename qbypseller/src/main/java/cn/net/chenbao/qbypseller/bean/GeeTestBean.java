package cn.net.chenbao.qbypseller.bean;

/**
 * Created by wuri on 2016/11/19.
 */

public class GeeTestBean {
    public GeeValidResultBean GeeValidResult;

    public static class GeeValidResultBean {
        public DataBean Data;
        public int ErrCode;
        public boolean HaveNextPage;
        public int PageCount;
        public boolean Success;
        public int TotalAmount;
        public int TotalAmount2;
        public int TotalCount;


        public static class DataBean {
            public String CaptchaId;
            public String Challenge;
            public String ClientId;
            public int Success;

        }
    }
}
