pragma solidity ^0.4.10;

contract copyrightDemo{
    //0x17acfb54a3c64227a983ed54c4763424f06b8a9e
    //用户
    struct user {
        uint64 userPhone ;
        string date;
        int value;
    }

    //确权公证信息
    struct confirm{
        uint64 userPhone ;
        uint64 tag;
        string imgName ;
        string key ;
    }
    //交易
    struct trade{
        uint64 purchasePhone ;
        uint64 sellerPhone ;
        uint64 price ;
        uint64 tag ;
        string imgName;
        string key;
    }

    mapping(uint64 => user) Users;   //用户

    mapping(uint64 => confirm) ConfirmMap ;

    mapping(string => trade) TradeMap;     //交易map

    /*
    描述 : 初始化用户
    参数 ：
            ID : 资产账户
    返回值：
            1  初始化成功
            -1 初始化失败
            不能将int转换成字符串类型，留在java调用中拼接
    */
    function CreateUser(uint64 userPhone,string date) public returns(int){
        Users[userPhone] = user(userPhone,date,100);  //初始
    	   // UserInfo(ID,Users[ID].allValue);    //触发事件
    	return 1;
    }

    //素材侵权公证
    // 返回交易哈希和code
    function Confirm(uint64 userPhone,uint64 tag,string imgName,string key) public {
        ConfirmMap[userPhone] = confirm(userPhone,tag,imgName,key);
    }

    //==================================增加交易函数=====================================================//
    function Trade(uint64 purchasePhone,uint64 sellerPhone,uint64 price,uint64 tag,string imgName,string key) public{
        TradeMap[imgName] = trade(purchasePhone,sellerPhone,price,tag,imgName,key);
        Users[purchasePhone].value -= price;
        Users[sellerPhone].value += price;
    }

    /*
    描述 : 查询用户资产情况
    参数 ：
        ID : 资产账户
    */
     function QueryValue(uint64 userPhone)public constant returns(int){
        return Users[userPhone].value;
    }

}

