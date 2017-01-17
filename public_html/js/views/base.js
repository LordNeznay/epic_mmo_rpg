define([
    'backbone',
    'models/player'
], function(
    Backbone,
    Player
){

    var Base = Backbone.View.extend({
        el: '.page',
        player: Player,
        
        initialize: function(options){
            this.player.status();
            if(options.content) {
                this.el = options.content;
            }
            this.render();
        },
        
        render: function() {
            this.$el.append(this.template());
            this.$el = $(this.el);
        },

        show: function() {
            this.player.status();
            this.trigger('show', this);
            $(this.el).show();      
        },

        hide: function() {
            $(this.el).hide();
        },
        
    });    

    return Base;
});