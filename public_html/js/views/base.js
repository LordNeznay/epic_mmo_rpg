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
        },
        
        render: function() {
            $(this.options).remove();
            this.$el.append(this.template());
        },

        show: function() {
            this.player.status();
            this.trigger('show', this);
            $(this.content).show();           
        },

        hide: function() {
            $(this.content).hide();
        },
        
    });    

    return Base;
});