define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
		defaults: {
			name: "username",
			score: 0,
			isLogin: false
		},
		isLogin: function(){
			return this.isLogin;
		},
		tryLogin: function(params){
			$.ajax({
				type: "POST",
				data: params,
				url: "/api/v1/auth/signin",
				dataType: 'json',
				success: function(data) {
					//if(data.errors != 'null'){
					return data.errors;
				}
			});
		},
		unlogin: function(){
			$.ajax({
				type: "POST",
				url: "/api/v1/auth/exit"
			});    
		}
	});

    return Model;
});