## 订单评论功能

#### 统一评论格式
```json
{
  "orderNo":"s0001",
  "items":[
    {
      "name":"xxx",
      "comment":"-",
      "score":4,
      "complaintLevel":0,
      "effective":0,
      "departmentCode": "",
      "payment":"15.4",
      "refund":"15.4",
      "salesMaintain":1,
      "representationReason":"-",
      "representationResource":[
        {
          "url":"-"
        }
      ],
      "representationResult":"",
      "representationComment":""
    }  
  ],
  "channel":"x",
  "commentDateTime": "2020-06-11-12 13:14:41",
  "score": 5,
  "disCode":"q0001",
  "storeCode": "x0001",
  "complaintChannel":"xx",
  "complaintStatus": 1,
  "issueType": 1,
  "createdOrderDateTime": "2020-05-16 16:16:17",
  "receiveOrderDateTime": "2020-05-17 16:16:17",
  "complaintLevel":"x",
  "effective": 0,
  "judgement": 1,
  "departmentCode": "d0001",
  "salesMaintain":1,
  "refund": 0,
  "payment": 0,
  "areaCode": "a0001",
  "dispatchChannel": 1,
  "cityCode": "xxx",
  "representationReason": "",
  "representationResource":[
    {
      "url":"-"
    }  
  ],
  "representationResult":"",
  "reply": 0,
  "operator": "lisi",
  "images":[
    {
      "url":"-"
    }  
  ],
  "receiver":{
    "phone":"-"
  },
  "comments":[
    {
      "comment":"-"
    }
  ]
}
```

#### 存储字段说明
| 字段 | 注释 | 是否必填 | 默认值| 备注 |
| :--- | :----: | :----: | :----: | :----:|
|orderNo| 订单编号| Y |||
|items |  ||||
|items.name| 商品名称| Y | | |
|items.comment|商品评论|N| - | |
|items.score|评分|Y|5| |
|items.complaintLevel|投诉代码|Y|0|固定的枚举值|
|items.effective|是否有效 | Y |0 |默认无效|
|items.departmentCode| 部门编号|Y||门店域获取|
|items.payment|商品支付|Y|||
|items.refund|退款|Y|||退款不能大于商品支付价格|
|items.salesMaintain|售后|Y|||
|items.representationReason|申诉理由|Y|-||
|items.representationResource.url|申诉资料|Y|-||
|items.representationResult|申诉结果|Y|-||
|items.representationComment|申诉评论|N|-||
|channel|销售渠道|Y|||
|commentDateTime|评论时间|Y|||
|score|评分|Y|5|默认是好评|
|disCode|配送中心代码|Y||门店域获取|
|storeCode|门店代码|Y|||
|complaintChannel|投诉渠道|Y|-||
|complaintStatus|投诉状态|Y|0|没有投诉|
|issueType|问题类别|Y|0|没有问题类别|
|createdOrderDateTime|订单创建时间|Y|||
|receiveOrderDateTime|订单送达时间|Y||oms系统获取|
|complaintLevel|投诉等级|Y|0|没有投诉|
|effective|是否有效|Y|0|默认无效|
|judgement|申诉结果|Y|-||
|departmentCode|部门code|N||门店域获取|
|salesMaintain|售后|Y|0|售后类型|
|payment|支付|Y|||
|refund|退款|Y||退款不可以大于实际金额|
|areaCode|大区代码|Y||门店域去获取|
|dispatchChannel|配送渠道|Y||固定的枚举值|
|cityCode|城市Code|Y|||
|representationReason|申诉理由|Y|||
|representationResource||||
|url|申诉图片|N|||
|representationResult|申诉结果|N|||
|reply|回评|N|-|默认是没有的|
|operator|操作员|Y||不允许是空|
|images||||
|url|申诉图片|N|-||
|receiver||||
|phone|收货人电话|N|-||
|comments||||
|comment|订单评论|N|||


## 创建订单评论

```text
url: /order-comment/api/v1/comment/created
method: post
```

#### request

```json
{
  "orderNo":"s0001",
  "items":[
    {
      "name":"xxx",
      "comment":"-",
      "score":4,
      "complaintLevel":0,
      "effective":0,
      "departmentCode": "",
      "payment":"15.4",
      "refund":"15.4",
      "salesMaintain":1,
      "representationReason":"-",
      "representationResource":[
        {
          "url":"-"
        }
      ],
      "representationResult":"",
      "representationComment":""
    }  
  ],
  "channel":"x",
  "commentDateTime": "2020-06-11-12 13:14:41",
  "score": 5,
  "disCode":"q0001",
  "storeCode": "x0001",
  "complaintChannel":"xx",
  "complaintStatus": 1,
  "issueType": 1,
  "createdOrderDateTime": "2020-05-16 16:16:17",
  "receiveOrderDateTime": "2020-05-17 16:16:17",
  "complaintLevel":"x",
  "effective": 0,
  "judgement": 1,
  "departmentCode": "d0001",
  "salesMaintain":1,
  "refund": 0,
  "payment": 0,
  "areaCode": "a0001",
  "dispatchChannel": 1,
  "cityCode": "xxx",
  "representationReason": "",
  "representationResource":[
    {
      "url":"-"
    }  
  ],
  "representationResult":"",
  "reply": 0,
  "operator": "lisi",
  "images":[
    {
      "url":"-"
    }  
  ],
  "receiver":{
    "phone":"-"
  },
  "comments":[
    {
      "comment":"-"
    }
  ]
}
```

#### response
```json
{
  "resultCode": 0,
  "errorMsg":"",
  "data": {}
}
```

## 查询默认订单评论

```text
url: /order-comment/api/v1/${operatorName}/view/latest/comments
method: post
```

#### request

// 1 = 1 and  （storeCode = "0081" or storeCode = "0081""）  and commentDateTime between 2019-12-11 13:12:11 and 2019-12-12 13:12:11

```json
{
  "logical": "must",
  "conditions": [
    {
      "logical": "should",
      "conditions": [
        {
          "op":"match",
          "fieldName":"storeCode",
          "type":"text",
          "values":["0081","0087"]
        }  
      ]
    },
    {
      "op": "range",
      "fieldName": "commentDateTime",
      "type": "date",
      "values": [
        "2019-12-11 13:12:11",
        "2019-12-12 13:12:11"
      ]
    }
  ]
}
```

#### response
```json
{
  "resultCode": 0,
  "errorMsg": "",
  "data": {
    "page": 10,
    "payLoad": [
      {
            "orderNo": "s0001",
            "items": [
              {
                "name": "xxx",
                "comment": "-",
                "score": 4,
                "complaintLevel": 0,
                "effective": 0,
                "departmentCode": "",
                "payment": "15.4",
                "refund": "15.4",
                "salesMaintain": 1,
                "representationReason": "-",
                "representationResource": [
                  {
                    "url": "-"
                  }
                ],
                "representationResult": "",
                "representationComment": ""
              }
            ],
            "channel": "x",
            "commentDateTime": "2020-06-11-12 13:14:41",
            "score": 5,
            "disCode": "q0001",
            "storeCode": "x0001",
            "complaintChannel": "xx",
            "complaintStatus": 1,
            "issueType": 1,
            "createdOrderDateTime": "2020-05-16 16:16:17",
            "receiveOrderDateTime": "2020-05-17 16:16:17",
            "complaintLevel": "x",
            "effective": 0,
            "judgement": 1,
            "departmentCode": "d0001",
            "salesMaintain": 1,
            "refund": 0,
            "payment": 0,
            "areaCode": "a0001",
            "dispatchChannel": 1,
            "cityCode": "xxx",
            "representationReason": "",
            "representationResource": [
              {
                "url": "-"
              }
            ],
            "representationResult": "",
            "reply": 0,
            "operator": "jack",
            "images": [
              {
                "url": "-"
              }
            ],
            "receiver": {
              "phone": "-"
            },
            "comments": [
              {
                "comment": "-"
              }
            ]
          }
    ]   
  } 
}
```

#### 字段说明

| 字段 | 注释 | 是否必填 | 默认值| 备注 |
| resultCode | 状态码| N | 0 |0表示没有错误,其余按约定逻辑码 |
| errorMsg | 错误消息 | N | 长度为0的空字符串 |服务端错误信息 |
|data||
|data.page| 总页码数 | Y | | |
|data.payLoad| 查询的类容| Y |||










