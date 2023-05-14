package com.ptu.util;

import com.ptu.entity.HbaseInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * 1、HBaseConfiguration：封装了HBase集群的配置信息（代码运行所需要的环境）
 * 2、HBaseAdmin：HBase系统管理员的角色，提供了对数据表进行操作或者管理的一些方法
 * 3、HTable：封装了整个表的信息（表名、列簇的信息），提供了操作该表数据的所有的业务方法
 * 4、HTableDescriptor：所有的列簇的信息（一到多个HColumnDescriptor）
 * 5、HColumnDescriptor：一个列簇的信息
 * 6、Cell：封装了一个column的信息：行键、列簇、列、值、时间戳
 * 7、Put：插入操作所需要的所有的相关信息
 * 8、Delete：删除操作所需要的所有的相关信息
 * 9、Get：封装查询条件
 * 10、Scan：封装所有的查询信息，和Get有一点不同：Scan可以设置Filter
 * 11、Result：封装了一个rowKey所对应的所有的数据信息
 * 12、ResultScanner：封装了多个Result对应的结果集，本质上就是多个rowKey所对应的信息
 */
public class HBaseManger {
    public static void main(String[] args) throws IOException {
//        System.out.println(isTableExist("tbl_users"));

    }

    //获取Configuration对象
    public static Configuration conf;
    static{
        //使用HBaseConfiguration的单例方法实例化
        //HBaseConfiguration：封装了HBase集群的配置信息（代码运行所需要的环境）
//        conf = HBaseConfiguration.create();
//        conf.set("hbase.zookeeper.quorum","ccx80:2181,ccx81:2181,ccx82:2181");
//        conf.set("hbase.zookeeper.quorum","192.168.176.142");

    }

    //1、判断表是否存在
    public static boolean isTableExist(String tableName) throws IOException {
        //在HBase中管理、访问表需要先创建HBaseAdmin对象
        //HBaseAdmin：HBase系统管理员的角色，对数据表进行操作或者管理的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = (HBaseAdmin)connection.getAdmin();
        return admin.tableExists(TableName.valueOf(tableName));
    }

    //2、创建表，String...columnFamily是不定长参数
    public static void createTable(String tableName, String...columnFamily) throws IOException {
        //创建HBaseAdmin对象，以获取对数据表进行操作或者管理的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = (HBaseAdmin) connection.getAdmin();
        //判断表是否存在
        if (isTableExist(tableName)){
            System.out.println("table:" + tableName + "已存在");
        }else{
            //创建表属性HTableDescriptor对象，表名需要转字节类型
            //HTableDescriptor：所有的列簇的信息（一到多个HColumnDescriptor）
            HTableDescriptor descriptor = new HTableDescriptor(TableName.valueOf(tableName));
            //创建多个列簇
            for (String cf : columnFamily){
                descriptor.addFamily(new HColumnDescriptor(cf));
            }
            //根据对表的配置，创建表
            admin.createTable(descriptor);
            System.out.println("table:" + tableName + "创建成功");
        }
    }

    //3、删除表：先disable再drop(delete)
    public static void dropTable(String tableName) throws IOException {
        //创建HBaseAdmin对象，以获取对数据表进行操作或者管理的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        //判断表是否存在，若存在，先disable再delete
        if (isTableExist(tableName)){
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
            System.out.println("table:" + tableName + "删除成功");
        }else{
            System.out.println("table:" + tableName + "不存在");
        }
    }

    //4、向表中插入数据
    public static void addData(String tableName,String rowKey,String columnFamily,String column,String value) throws IOException {
        //创建HTable对象，以获取操作该表数据的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        //向表中插入数据：先实例化一个Put对象，作为数据的载体
        Put put = new Put(Bytes.toBytes(rowKey));
        //向put对象中组装数据
        put.addColumn(columnFamily.getBytes(),column.getBytes(),value.getBytes());
        //向表中放入数据
        hTable.put(put);
        hTable.close();
        System.out.println("插入数据成功！");
    }

    //5、获取所有数据，也就是获取所有行
    public static List<HbaseInfo> getAllData(String tableName) throws IOException {
        //创建HTable对象，以获取操作该表数据的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        //得到用于扫描region的对象scan
        //Scan：封装所有的查询信息。和Get有一点不同：Scan可以设置Filter
        Scan scan = new Scan();
        //使用HTable得到resultScanner实现类的对象
        //ResultScanner：封装了多个Result的结果集，本质上不就是多个rowKey所对应的信息
        ResultScanner resultScanner = hTable.getScanner(scan);
        List<HbaseInfo> hbaseInfoList = new ArrayList<>();
        for (Result result : resultScanner) {
            //Cell：封装了Column的所有的信息：RowKey、column qualifier、value、时间戳
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                String rowKey = Bytes.toString(CellUtil.cloneRow(cell));
                String columnFamily = Bytes.toString(CellUtil.cloneFamily(cell));
                String column = Bytes.toString(CellUtil.cloneQualifier(cell));
                String value = Bytes.toString(CellUtil.cloneValue(cell));
                String time = String.valueOf(cell.getTimestamp());
                HbaseInfo hbaseInfo = new HbaseInfo(rowKey, columnFamily, column, value, time);
                hbaseInfoList.add(hbaseInfo);
            }
        }
        return hbaseInfoList;
    }

    //6、获取某行数据
    public static HbaseInfo getRowData(String tableName,String rowKey) throws IOException {
        //创建HTable对象，以获取操作该表数据的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        //得到用于扫描一行的对象get
        Get get = new Get(Bytes.toBytes(rowKey));//这里的get是行键rowKey的载体
        Result result = hTable.get(get); //使用get方法获取get中的一行数据
        //Cell：封装了Column的所有的信息：RowKey、column qualifier、value、时间戳
        Cell[] cells = result.rawCells();
        //循环获取所有信息
        HbaseInfo hbaseInfo = null;
        for (Cell cell : cells) {
            String RowKey=Bytes.toString(result.getRow());
            String columnFamily = Bytes.toString(CellUtil.cloneFamily(cell));
            String column = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            String time= String.valueOf(cell.getTimestamp());
            hbaseInfo = new HbaseInfo(RowKey, columnFamily, column, value,time);
        }
        return hbaseInfo;
    }

    //7、获取某行指定的数据，比如指定某个列簇的某个列限定符
    public static HbaseInfo getRowQualifierData(String tableName,String rowKey,String family,String qualifier) throws IOException {
        //创建HTable对象，以获取操作该数据表的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        //得到用于扫描一行的对象get
        Get get = new Get(Bytes.toBytes(rowKey));//这里的get是行键的载体
        get.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier));//将具体信息往get载体中添加
        Result result = hTable.get(get);//使用get方法获取get中的数据
        //Cell：封装了Column的所有的信息：RowKey、column qualifier、value、时间戳
        Cell[] cells = result.rawCells();
        //循环获取所有信息,也可以单独打印自己需要的字段即可，这个一般根据业务需求修改。
        HbaseInfo hbaseInfo = null;
        for (Cell cell : cells) {
            String RowKey=Bytes.toString(result.getRow());
            String columnFamily = Bytes.toString(CellUtil.cloneFamily(cell));
            String column = Bytes.toString(CellUtil.cloneQualifier(cell));
            String value = Bytes.toString(CellUtil.cloneValue(cell));
            String time= String.valueOf(cell.getTimestamp());
            hbaseInfo = new HbaseInfo(RowKey, columnFamily, column, value,time);
        }
        return hbaseInfo;
    }

    //8、删除单行或多行数据
    public static void deleteRowsData(String tableName,String... rows) throws IOException {
        //创建HTable对象，以获取操作该数据表的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        List<Delete> deleteList = new ArrayList<Delete>();
        //循环
        for (String row : rows) {
            Delete delete = new Delete(Bytes.toBytes(row));
            deleteList.add(delete);
        }
        //执行删除操作
        hTable.delete(deleteList);
        hTable.close();
        System.out.println("行删除成功！");
    }

    //9、删除某行指定的数据，比如指定某个列簇的某个列限定符
    public static void deleteRowsQualifierData(String tableName,String rowKey,String family,String qualifier) throws IOException {
        //创建HTable对象，以获取操作该数据表的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        //创建删除对象
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        //设置删除的列
        delete.addColumn(Bytes.toBytes(family),Bytes.toBytes(qualifier));
        //执行删除操作
        hTable.delete(delete);
        //关闭表连接
        hTable.close();
        System.out.println("列删除成功");
    }

    //10、获取某行至某行的数据
    public static void getSSRowData(String tableName,String startRow,String stopRow) throws IOException {
        //创建HTable对象，以获取操作该数据表的一些方法
        Connection connection = ConnectionFactory.createConnection(conf);
        Table hTable = connection.getTable(TableName.valueOf(tableName));
        //创建用于扫描Region的对象Scan
        Scan scan = new Scan();
        //设置扫描的开始行和结束行
        scan.withStartRow(Bytes.toBytes(startRow));
        scan.withStopRow(Bytes.toBytes(stopRow));
        ResultScanner resultScanner = hTable.getScanner(scan);
        //循环
        for (Result result : resultScanner) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                System.out.println("行键: " + Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.println("列簇: " + Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("列: " + Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("值: " + Bytes.toString(CellUtil.cloneValue(cell)));
                System.out.println();
            }
        }
    }
    //11.查询数据库中的表
    public static void listTables() throws IOException {
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        HTableDescriptor hTableDescriptors[] = admin.listTables();
        for (HTableDescriptor hTableDescriptor : hTableDescriptors) {
            System.out.println(hTableDescriptor.getNameAsString());
        }
    }


    public static ResultScanner getPageData(int pageIndex,int pageSize,String tableName) throws Exception{

        if (pageSize < 3 || pageSize > 15) {
            pageSize = 5;
        }
        String startRow = GetCurrentPageStartRow(pageIndex,pageSize,tableName);
        return getPageData(startRow,pageSize,tableName);
    }

    /**
     * 	当前这个方法的作用：
     *
     * 	就是把前端传送过来的pageIndex转换成startRow
     *
     *  以方便调用底层最简单的获取一页分页数据的 方法： getPageData(startRow, pageSize)
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private static String GetCurrentPageStartRow(int pageIndex, int pageSize,String tableName) throws Exception {

        // 如果 传送过来的额  pageIndex 不合法。 默认返回 第一页数据
        if (pageIndex <=1) {
            return null;
        }else {
            //从第二页开始的所有数据。
            String startRow = null;

            for (int i = 1; i < pageIndex - 1; i++) {
                //第几次循环，就是获取第几页的数据
                ResultScanner pageData = getPageData(startRow, pageSize,tableName);
                //获取当前这一页的最后roekey
                Iterator<Result> iterator = pageData.iterator();
                Result result = null;
                while (iterator.hasNext()) {

                    result = iterator.next();
                }
                //让最后一个rowkey往后挪动一点位置，但是又不会是下一页的startRow
                String endRowStr = new String(result.getRow());
                byte[] add = Bytes.add(endRowStr.getBytes(), new byte[]{ 0x00});

                String nextPageStartRowstr = Bytes.toString(add);

                startRow = nextPageStartRowstr;
            }
            return startRow;
        }
    }
    /**
     * 描述：
     *
     * 	从  startRow开始 查询 pageSize 条数据
     *
     * @param startRow
     * @param pageSize
     * @return
     */
    private static ResultScanner getPageData(String startRow, int pageSize,String tableName) throws Exception {

        Connection con = ConnectionFactory.createConnection(conf);
        Table table = con.getTable(TableName.valueOf(tableName));

        Scan scan = new Scan();

        //如果是第一页数据，所以scan.setStartRow这句代码根本就没有任何意义，不用设置即可
        if (!StringUtils.isBlank(startRow)) {

            //如果用户不传入startRow，或者传入一个非法的startRow，还是按照规则返回第一页
            scan.setStartRow(startRow.getBytes());
        }
        //设置总数据条件
        Filter pageFilter = new PageFilter(pageSize);
        scan.setFilter(pageFilter);
        ResultScanner scanner = table.getScanner(scan);

        return scanner;
    }

}


