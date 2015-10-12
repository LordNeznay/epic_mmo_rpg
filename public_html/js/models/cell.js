define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
		defaults: {
			
			gid: "",
			x: 0,
			y: 0,
		}
    });

    return Model;
});