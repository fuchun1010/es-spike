```shell script
curl -XPOST "http://localhost:3900/order/query" -H "Content-Type:application/json" -d '{
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
}'
```

```shell script
curl -XPOST "http://localhost:3900/order/query" -H "Content-Type:application/json" -d '
{
  "logical": "must",
  "conditions": [
    {
      "logical": "should",
      "conditions": [
        {
          "op": "term",
          "fieldName": "storeCode",
          "type": "text",
          "values": [
            "0081"
          ]
        },
        {
          "logical": "must",
          "conditions": [
            {
              "op": "term",
              "fieldName": "age",
              "type": "text",
              "values": ["11"]
            },
            {
              "op": "term",
              "fieldName": "gender",
              "type": "text",
              "values": ["male"]
            }
          ]
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
}'
```