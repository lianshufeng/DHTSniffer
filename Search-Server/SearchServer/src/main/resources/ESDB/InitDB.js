### 删除库
DELETE /resstore

### 创建索引库
PUT /resstore

### 设置设置最大
PUT /resstore/_settings
{
  "index.mapping.total_fields.limit": 102400,
  "index.refresh_interval": "120s",
  "max_result_window" : 1000000
}


### 创建分词表与字段
POST /resstore/Record/_mapping
{

        "properties": {
          "index": {
                "type": "text",
                "analyzer": "ik_max_word",
                "search_analyzer": "ik_max_word"
            },
            "hit": {
                "type": "long"
            },
            "tags": {
                "type": "text",
                "analyzer": "ik_max_word",
                "search_analyzer": "ik_max_word"
            },
            "title": {
                "type": "text",
                "analyzer": "ik_max_word",
                "search_analyzer": "ik_max_word"
            }
        }
}


### 测试数据
POST /resstore/Record
{"title":"美国留给伊拉克的是个烂摊子吗","index":"美国留给伊拉克的是个烂摊子吗","url":"http://url.com/dl.html","files":{"/path/ss.mp3":6901795,"/path/cc.exe":9375680,"/path/aa.mp4":2950452,"/path/demo.rmvb":936992},"totalSize":20164919,"createTime":1521195028672,"hitCount":1}

POST /resstore/Record
{"title":"公安部：各地校车将享最高路权","index":"公安部：各地校车将享最高路权","url":"http://url.com/dl.html","files":{"/path/ss.mp3":7882086,"/path/cc.exe":9406175,"/path/aa.mp4":4932380,"/path/demo.rmvb":693502},"totalSize":22914143,"createTime":1521195029027,"hitCount":1}

POST /resstore/Record
{"title":"中韩渔警冲突调查：韩警平均每天扣1艘中国渔船","index":"中韩渔警冲突调查：韩警平均每天扣1艘中国渔船","url":"http://url.com/dl.html","files":{"/path/ss.mp3":7742669,"/path/cc.exe":781037,"/path/aa.mp4":9579469,"/path/demo.rmvb":905677},"totalSize":19008852,"createTime":1521195029027,"hitCount":1}

POST /resstore/Record
{"title":"中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首","index":"中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首","url":"http://url.com/dl.html","files":{"/path/ss.mp3":633763,"/path/cc.exe":2469715,"/path/aa.mp4":1577033,"/path/demo.rmvb":515164},"totalSize":5195675,"createTime":1521195029027,"hitCount":1}


### 测试检索
POST /resstore/Record/_search
{
    "query" : { "match" : { "title" : "每天" }},
    "highlight" : {
        "pre_tags" : ["<tag1>", "<tag2>"],
        "post_tags" : ["</tag1>", "</tag2>"],
        "fields" : {
            "title" : {}
        }
    }
}

###查询一条记录
GET /resstore/Record/_mget
{
    "docs" : [
        {
            "_id" : "m8Ilr2IBK1pY2aVnzuEx"
        }
    ]
}

