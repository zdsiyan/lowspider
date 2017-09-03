# slowspider
解析html就快不了, 也没打算做成高效的, 个人用爬虫慢点也够用了. 准备做一个支持 css query, xpath的可配爬虫中间件. 根据配置, 爬取文章后通过模板转换为html, 并发送邮件.完成后会再做一款GAE, 方便搭建7*24小时个人服务.


## 1. 迭代计划

* css query测试
* 正则解析测试
* 定制模板测试
* 处理bug， 去掉多余的配置， 重构优化
* 建引用模式测试



## 2. 已完成

* 2017-8-26 传入markdown模板+freemarker变量的形式, 方便自组织布局
* 2017-9-2 测试完爬全本,部分链接。 默认的模板美感差一些, 以后支持定制, 就不要吐槽了吧
* 2017-9-3 mail数据流发送测试完毕


## 3. 配置示例， 转json呈现

```

	{
		"email":{
			"auth":true,
			"enable":true,
			"required":true,
			"filename":"我的阅读日常.html",
			"from":"你的邮箱地址",
			"host":"smtp.163.com",
			"username":"你的邮箱名（无需@）",
			"password":"你的邮箱密码",
			"title":"我的阅读日常",
			"to":"你的目标邮箱地址"
		},
		"name":"我的阅读日常",
		"nodeConfig":[
			{
				"content":{
					"replace":{
						"飘天文学感谢各位书友的支持，您的支持就是我们最大的动力":"",
						"\\{\\}":""
					},
					"xpath":"html/body/text()"
				},
				"link":"http://www.piaotian.com/html/8/8755/",
				"links":{
					"xpath":"//div[@class='centent']//a"
				},
				"name":"少年医仙",
				"site":"http://www.piaotian.com/html/8/8755/",
				"title":{
					"replace":{
						"正文":""
					},
					"xpath":"html/body/h1/text()"
				}
			}
		]
	}
	
```
