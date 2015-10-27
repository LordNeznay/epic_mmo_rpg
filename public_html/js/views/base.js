define([
    'backbone',
    'models/player'
], function(
    Backbone,
    Player
){

    var Base = Backbone.View.extend({
        el: '.page',
        player: new Player(),
        
        initialize: function(options){
            this.player.status();
            if(options.content) {
                this.content = options.content;
            }
            this.render();
            if(this.child_init != undefined){
                this.child_init();
            }
        },
        
        render: function() {
            this.$el.append(this.template());
            if(this.child_render != undefined){
                this.child_render();
            }
        },

        show: function() {
            this.player.status();
            this.trigger('show', this);
            if(this.child_show != undefined){
                this.child_show();
            }
            $(this.content).show();           
        },

        hide: function() {
            if(this.child_hide != undefined){
                this.child_hide();
            }
            $(this.content).hide();
        },
        
    });    

    return Base;
});