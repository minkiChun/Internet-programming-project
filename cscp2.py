#!/usr/bin/env python
#-*- coding: utf-8 -*-

import pymysql
import sys
from konlpy.tag import Kkma
from konlpy.utils import pprint

reload (sys)
sys.setdefaultencoding('utf-8')

kkma = Kkma()
list = kkma.nouns(sys.argv[1])

# MySQL Connection 연결
conn = pymysql.connect(host='localhost', user='tester', password='', db='testdb', charset='utf8')

try:
	with conn.cursor() as curs:
		sql = "select"
		sql += list
		sql += "from customer"
		curs.execute(sql)
		rows = curs.fetchall()
		print (rows)

finally:
	conn.close()