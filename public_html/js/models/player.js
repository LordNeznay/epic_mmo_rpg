define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        defaults: {
            name: "",
            isLogin: false,
            score: 0,
            ws: null
        },
        socket_open: function(){
            if(!isLogin) return;
            
            this.ws = new WebSocket('ws://' + document.location.host + '/gameplay');
            this.ws.onopen = function (event) {

            }
            this.ws.onmessage = function (event) {
                var data = JSON.parse(event.data);
                console.log(data);
            }
            this.ws.onclose = function (event) {

            }
        },
        status: function(callback){
            var that = this;
            $.ajax({
                type: "GET",
                url: "/api/v1/auth/signin",
                dataType: 'json',
                success: function(data){	
                    if(data.isLogin == 'true'){
                        that.isLogin = true;
                    } else {
                        that.isLogin = false;
                    }
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });        
        },
        login: function(param, callback){
            var that = this;
            $.ajax({
                type: "POST",
                data: param,
                url: "/api/v1/auth/signin",
                dataType: 'json',
                success: function(data){
                    if(data.errors == 'null'){
                        that.isLogin = true;
                    }
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });
        },
        unlogin: function(callback){
            var that = this;
            $.ajax({
                type: "POST",
                url: "/api/v1/auth/exit",
                success: function(data){
                    that.isLogin = false;
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });    
        },
        registration: function(param, callback){
            var that = this;
            $.ajax({
                type: "POST",
                data: param,
                url: "/api/v1/auth/signup",
                dataType: 'json',
                success: function(data) {
                    if(callback != undefined && callback.success != undefined){
                        return callback.success(data);
                    }
                }
            });
        },
        
        
        getCoord: function(){
            var message = '{"command": "getcoord"}';
            ws.send(message);
        }
    });

    return Model;
});