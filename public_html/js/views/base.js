define([
    'backbone',
], function(
    Backbone
){

    var Base = Backbone.View.extend({
        el: '.page',
        
        initialize: function(options) {
            if(options.content) {
                this.content = options.content;
            }
            this.render();
            if(this.child_init != undefined){
                this.child_init();
            }
        },
        
        render: function () {
            this.$el.append(this.template());
            if(this.child_render != undefined){
                this.child_render();
            }
        },

        show: function () {
            this.trigger('show', this);
            if(this.child_show != undefined){
                this.child_show();
            }
            $(this.content).show();           
        },

        hide: function () {
            $(this.content).hide();
        },
        
    });    

    return Base;
});