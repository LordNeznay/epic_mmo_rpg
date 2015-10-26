define([
    'backbone'
], function(
    Backbone
){

    var Model = Backbone.Model.extend({
        gid: 0,
        x: 0,
        y: 0,
        view_x: 0,
        view_y: 0,
        defaults: {
            gid: 0,
            x: 0,
            y: 0,
            view_x: 0,
            view_y: 0,
        }
    });

    return Model;
});