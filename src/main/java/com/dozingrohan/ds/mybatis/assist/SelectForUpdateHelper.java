package com.dozingrohan.ds.mybatis.assist;

/**
 * @Description: 分页辅助
 * @author:		DozingRohan
 * @version: 	0.0.1
 * @createDate: 2020年6月25日 下午6:07:03
 */
public class SelectForUpdateHelper {
	
	private static String updateSql = " FOR UPDATE ";
	
    private static ThreadLocal<Boolean> selectForUpdateholder = new ThreadLocal<Boolean>() {
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

	public static void setSelectForUpdate() {
		selectForUpdateholder.set(true);
	}
	
	public static void cancelSelectForUpdate() {
		selectForUpdateholder.set(false);
	}
	
	public static boolean isSelectForUpdate() {
        return selectForUpdateholder.get();
    }
	
	public static String getUpdateSql() {
        return updateSql;
    }
	
	
	/**
	 * @Description: 设置 wait time
	 */
    public void setWaitTime(String waitTime) {
        String updateSql = waitTime.toUpperCase();
        if (updateSql.length() > 0 && updateSql.indexOf("WAIT") == -1) {
            updateSql = "WAIT " + waitTime;
        }
        SelectForUpdateHelper.updateSql = " FOR UPDATE " + updateSql;
    }
}
