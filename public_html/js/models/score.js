define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
    	name: "",
    	score: 0
    });

    return Model;
});