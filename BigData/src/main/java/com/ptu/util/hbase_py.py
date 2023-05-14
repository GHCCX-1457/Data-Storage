# coding=utf-8

from thrift.transport import TSocket
from hbase import Hbase
from hbase.ttypes import *
import pymysql


def main(TableName):
    # 打开hbase数据库连接
    transport = TSocket.TSocket('192.168.176.142', 9090)
    protocol = TBinaryProtocol.TBinaryProtocol(transport)
    client = Hbase.Client(protocol)
    transport.open()

    # 定义列族
    cf1 = ColumnDescriptor(name='detail')

    # 建立表结构
    try:
        # 判断表是否存在

        tables_list = client.getTableNames()
        if TableName in tables_list:
            # 如果表存在则删除重新建立
            client.disableTable(TableName)
            client.deleteTable(TableName)
            client.createTable(TableName, [cf1])
        else:
            # 如果不存在，则创建表
            client.createTable(TableName, [cf1])
    except:
        print("创建表失败！")

    # 打开mysql数据库连接
    db = pymysql.connect(host="localhost", user="root", passwd="123456", database="ptu")
    # 使用 cursor() 方法创建一个游标对象 cursor
    cursor = db.cursor()
    # SQL 查询学生表信息
    sql = "SELECT * FROM {}".format(TableName)
    try:
        # 执行SQL语句
        cursor.execute(sql)
        # 获取所有记录列表
        Info = cursor.fetchall()
        i = 1
        for row in Info:
            code = row[0]
            name = row[1]
            print(code, name)
            mutations = [Mutation(column="detail:code", value=code),
                         Mutation(column="detail:name", value=name)]
            client.mutateRow(TableName, str(i), mutations)
            i += 1
        print('已插入', i, '条记录')
    except Exception as err:
        print(err)
    cursor.close()
    # 关闭数据库连接
    transport.close()
    db.close()


if __name__ == '__main__':
    main(sys.argv[0])
