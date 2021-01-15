package com.company.jdbc210112.test3_blob;

import com.company.jdbc210112.test2_preparedstatement.utils.JDBCUtiils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 使用 PreparedStatement 实现批量操作；主要是 insert
 * update 、 delete 自身具有批量操作的效果
 * 创建表：
 */
public class InsertTest {
    private static String names = "蒸羊羔、蒸熊掌、蒸鹿尾儿、烧花鸭、烧雏鸡、烧子鹅、卤猪、卤鸭、酱鸡、腊肉、松花小肚儿、晾肉、香肠儿、什锦苏盘、熏鸡白肚儿、清蒸八宝猪、江米酿鸭子、罐儿野鸡、罐儿鹌鹑、卤什件儿、卤子鹅、山鸡、兔脯、菜蟒、银鱼、清蒸哈什蚂、烩鸭丝、烩鸭腰、烩鸭条、清拌鸭丝、黄心管儿、焖白鳝、焖黄鳝、豆豉鲇鱼、锅烧鲤鱼、烀烂甲鱼、抓炒鲤鱼、抓炒对儿虾、软炸里脊、软炸鸡、什锦套肠儿、卤煮寒鸦儿、麻酥油卷儿、熘鲜蘑、熘鱼脯、熘鱼肚、熘鱼片儿、醋熘肉片儿、烩三鲜、烩白蘑、烩鸽子蛋、炒银丝、烩鳗鱼、炒白虾、炝青蛤、炒面鱼、炒竹笋、芙蓉燕菜、炒虾仁儿、烩虾仁儿、烩腰花儿、烩海参、炒蹄筋儿、锅烧海参、锅烧白菜、炸木耳、炒肝尖儿、桂花翅子、清蒸翅子、炸飞禽。炸汁儿、炸排骨、清蒸江瑶柱、糖熘芡仁米、拌鸡丝、拌肚丝、什锦豆腐、什锦丁儿、糟鸭、糟熘鱼片儿、熘蟹肉、炒蟹肉、烩蟹肉、清拌蟹肉、蒸南瓜、酿倭瓜、炒丝瓜、酿冬瓜.烟鸭掌儿、焖鸭掌儿、焖笋、炝茭白、茄子晒炉肉、鸭羹、蟹肉羹、鸡血汤、三鲜木樨汤、红丸子、白丸子、南煎丸子、四喜丸子、三鲜丸子、氽丸子、鲜虾丸子、鱼脯丸子、饹炸丸子、豆腐丸子、樱桃肉、马牙肉、米粉肉、一品肉、栗子肉、坛子肉、红焖肉、黄焖肉、酱豆腐肉、晒炉肉、炖肉、黏糊肉、烀肉、扣肉、松肉、罐儿肉、烧肉、大肉、烤肉、白肉、红肘子、白肘子、熏肘子、水晶肘子、蜜蜡肘子、锅烧肘子、扒肘条、炖羊肉、酱羊肉、烧羊肉、烤羊肉、清羔羊肉、五香羊肉、氽三样儿、爆三样儿、炸卷果儿、烩散丹、烩酸燕儿、烩银丝、烩白杂碎、氽节子、烩节子、炸绣球、三鲜鱼翅、栗子鸡、氽鲤鱼、酱汁鲫鱼、活钻鲤鱼、板鸭、筒子鸡、烩脐肚、烩南荠、爆肚仁儿、盐水肘花儿、锅烧猪蹄儿、拌稂子、炖吊子、烧肝尖儿、烧肥肠儿、烧心、烧肺、烧紫盖儿、烧连帖、烧宝盖儿、油炸肺、酱瓜丝儿、山鸡丁儿、拌海蜇、龙须菜、炝冬笋、玉兰片、烧鸳鸯、烧鱼头、烧槟子、烧百合、炸豆腐、炸面筋、炸软巾、糖熘饹儿、拔丝山药、糖焖莲子、酿山药、杏仁儿酪、小炒螃蟹、氽大甲、炒荤素儿、什锦葛仙米、鳎目鱼、八代鱼、海鲫鱼、黄花鱼、鲥鱼、带鱼、扒海参、扒燕窝、扒鸡腿儿、扒鸡块儿、扒肉、扒面筋、扒三样儿、油泼肉、酱泼肉、炒虾黄、熘蟹黄、炒子蟹、炸子蟹、佛手海参、炸烹儿、炒芡子米、奶汤、翅子汤、三丝汤、熏斑鸠、卤斑鸠、海白米、烩腰丁儿、火烧茨菰、炸鹿尾儿、焖鱼头、拌皮渣儿、氽肥肠儿、炸紫盖儿、鸡丝豆苗、十二台菜、汤羊、鹿肉、驼峰、鹿大哈、插根儿、炸花件儿，清拌粉皮儿、炝莴笋、烹芽韭、木樨菜、烹丁香、烹大肉、烹白肉、麻辣野鸡、烩酸蕾、熘脊髓、咸肉丝儿、白肉丝儿、荸荠一品锅、素炝春不老、清焖莲子、酸黄菜、烧萝卜、脂油雪花儿菜、烩银耳、炒银枝儿、八宝榛子酱、黄鱼锅子、白菜锅子、什锦锅子、汤圆锅子、菊花锅子、杂烩锅子、煮饽饽锅子、肉丁辣酱、炒肉丝、炒肉片儿、烩酸菜、烩白菜、烩豌豆、焖扁豆、氽毛豆、炒豇豆，外加腌苤蓝丝儿";
    private static final   String[] nameArr = names.split("、");

    public static void main(String[] args) {
        System.out.println(names.split("、").length);
    }

    //批量插入 - 本方法效率低，插入一条执行一次，IO 交互次数多
    @Test
    public void insertByPrepared1() {
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        String[] nameArr = names.split("、");
        try {
            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 0; i < 20000; i++) {
                ps.setObject(1, nameArr[i % nameArr.length]);
                ps.execute();
            }

            System.out.println("插入结束");
        } catch (Exception e) {
            System.out.println("插入报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
    }

    /**
     * 插入方式二：addBatch()、executeBatch()、clearBatch()
     * mysql 默认是关闭批处理的，我们需要通过一个参数，让 mysql 开启批处理的支持
     * 在 配置文件的 url 后面添加
     * 使用 更新的 mysql 驱动：mysql-connector-java-5.1.37-bin.jar
     */
    @Test
    public void insertByPrepared2() {
        long start = System.currentTimeMillis();
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        try {
            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, nameArr[i % nameArr.length]);

                //1. 攒 sql;
                ps.addBatch();
                if (i % 500 == 0) {
                    //2.执行一次 batch
                    ps.executeBatch();
                    //3.清空 batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("插入用时："+(end-start));//16660
        } catch (Exception e) {
            System.out.println("插入报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
    }

    /**
     * 插入方式三：设置 连接 不允许自动提交数据
     * 效率 提高到原来的 1/3 ；可能与及其有关，我并没有那么快
     */
    @Test
    public void insertByPrepared3() {
        long start = System.currentTimeMillis();
        Connection connection = JDBCUtiils.getConnection();
        PreparedStatement ps = null;
        try {
            //设置不允许自动提交数据
            connection.setAutoCommit(false);
            String sql = "insert into goods(name)values(?)";
            ps = connection.prepareStatement(sql);
            for (int i = 1; i <= 1000000; i++) {
                ps.setObject(1, nameArr[i % nameArr.length]);

                //1. 攒 sql;
                ps.addBatch();
                if (i % 500 == 0) {
                    //2.执行一次 batch
                    ps.executeBatch();
                    //3.清空 batch
                    ps.clearBatch();
                }
            }

            //统一提交数据
            connection.commit();
            long end = System.currentTimeMillis();
            System.out.println("插入用时："+(end-start));
        } catch (Exception e) {
            System.out.println("插入报错：" + e.getMessage());
        } finally {
            JDBCUtiils.closeConnection(ps, connection);
        }
    }

}
