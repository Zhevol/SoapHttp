package com.zhevol.soaphttp;

import java.util.List;

/**
 * //
 * Created by Administrator on 2018/3/8 0008.
 */

public class PassWordLogin {

    private String currentExceptionMessage;
    private DataBean data;
    private boolean success;

    public String getCurrentExceptionMessage() {
        return currentExceptionMessage;
    }

    public DataBean getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class DataBean {
        private AppuserBean appuser;
        private SysuserBean sysuser;

        public AppuserBean getAppuser() {
            return appuser;
        }

        public SysuserBean getSysuser() {
            return sysuser;
        }

        public static class AppuserBean {

            private String username;
            private String userphone;
            private int userid;
            private List<StudentcodesBean> studentcodes;

            public String getUsername() {
                return username;
            }

            public String getUserphone() {
                return userphone;
            }

            public int getUserid() {
                return userid;
            }

            public List<StudentcodesBean> getStudentcodes() {
                return studentcodes;
            }

            public static class StudentcodesBean {

                private int appuserstudentbindid;
                private int appuserid;
                private String studentcode;
                private String tmpstudentcode;
                private String usedstudentcode;
                private String othercode;
                private String oldcode;

                public int getAppuserstudentbindid() {
                    return appuserstudentbindid;
                }

                public int getAppuserid() {
                    return appuserid;
                }

                public String getStudentcode() {
                    return studentcode;
                }

                public String getTmpstudentcode() {
                    return tmpstudentcode;
                }

                public String getUsedstudentcode() {
                    return usedstudentcode;
                }

                public String getOthercode() {
                    return othercode;
                }

                public String getOldcode() {
                    return oldcode;
                }
            }
        }

        public static class SysuserBean {
            private String username;
            private String userphone;
            private int userid;
            private int academyid;
            private String academyname;
            private int academylocationid;

            public String getUsername() {
                return username;
            }

            public String getUserphone() {
                return userphone;
            }

            public int getUserid() {
                return userid;
            }

            public int getAcademyid() {
                return academyid;
            }

            public String getAcademyname() {
                return academyname;
            }

            public int getAcademylocationid() {
                return academylocationid;
            }
        }
    }
}
