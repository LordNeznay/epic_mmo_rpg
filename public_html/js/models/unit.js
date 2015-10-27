define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        defaults: {
            name: "",
            x: 10,
            y: 10
        },
    });

    return Model;
});