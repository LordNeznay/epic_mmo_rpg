define([
    'backbone'
], function(
    Backbone
){

    return function(method, model, options) {
        options.callback || (options.callback = {});

        switch (method) {
            case 'create': {
                //console.log('create');
                model.sync('read', this);
            } break;
            
            case 'update':{
                //console.log('update');
                var that = model;
                $.ajax({
                    type: "POST",
                    data: options.param,
                    url: "/api/v1/auth/signin",
                    dataType: 'json',
                    success: function(data){
                        if(data.errors == 'null'){
                            that.isLogin = true;
                        }
                        if(options.callback != undefined && options.callback.success != undefined){
                            return options.callback.success(data);
                        }
                    }
                }); 
            } break;
                
            case 'delete':{
                //console.log('delete');
                var that = model;
                $.ajax({
                    type: "POST",
                    url: "/api/v1/auth/exit",
                    success: function(data){
                        that.isLogin = false;
                        if(options.callback != undefined && options.callback.success != undefined){
                            return options.callback.success(data);
                        }
                    }
                });    
            } break;
                
            case 'read':{
                //console.log('read');
                var that = model;
                $.ajax({
                    type: "GET",
                    url: "/api/v1/auth/signin",
                    dataType: 'json',
                    success: function(data){	
                        //console.log(data.isLogin);
                        if(data.isLogin == 'true'){
                            that.isLogin = true;
                        } else {
                            that.isLogin = false;
                        }
                        if(options.callback != undefined && options.callback.success != undefined){
                            return options.callback.success(data);
                        }
                    }
                });                  
            } break;
            
            case 'singin':{
                //console.log('singin');
                var that = model;
                $.ajax({
                    type: "POST",
                    data: options.param,
                    url: "/api/v1/auth/signup",
                    dataType: 'json',
                    success: function(data) {
                        if(options.callback != undefined && options.callback.success != undefined){
                            return options.callback.success(data);
                        }
                    }
                });                
            } break;
            
            default:
                // Something probably went wrong
                console.error('Unknown method:', method);
                break;
          }

        
    };
});