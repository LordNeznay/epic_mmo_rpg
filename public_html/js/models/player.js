define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        defaults: {
            name: "",
            isLogin: false,
            score: 0
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
        }
    });

    return Model;
});