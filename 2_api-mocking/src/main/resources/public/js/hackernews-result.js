/*global jQuery, app */
(function ($) {
    'use strict';

    // Function to add HackernewsItemResult information to a todo item
    function addHackernewsResultInfo(todoView) {
        var model = todoView.model;
        var summary = model.get('summary');
        var priority = model.get('priority');
        var timeEstimate = model.get('timeEstimate');
        var sentiment = model.get('sentiment');

        // Only add the info if at least one of the fields is present
        if (summary || priority || timeEstimate || sentiment) {
            // Remove any existing hackernews-result div
            todoView.$('.hackernews-result').remove();

            // Create the hackernews-result div
            var resultDiv = $('<div class="hackernews-result"></div>');

            // Add summary if available
            if (summary) {
                resultDiv.append('<div class="summary">' + summary + '</div>');
            }

            // Add details div with priority, timeEstimate, and sentiment
            var detailsDiv = $('<div class="details"></div>');
            if (priority) {
                detailsDiv.append('<span class="priority">Priority: ' + priority + '</span>');
            }
            if (timeEstimate) {
                detailsDiv.append('<span class="time-estimate">Time: ' + timeEstimate + '</span>');
            }
            if (sentiment) {
                detailsDiv.append('<span class="sentiment">Sentiment: ' + sentiment + '</span>');
            }

            // Only add the details div if it has any content
            if (detailsDiv.children().length > 0) {
                resultDiv.append(detailsDiv);
            }

            // Add the result div to the todo item
            todoView.$('.view').append(resultDiv);
        }
    }

    // Override the render method of TodoView to add HackernewsItemResult information
    var originalRender = app.TodoView.prototype.render;
    app.TodoView.prototype.render = function () {
        var result = originalRender.apply(this, arguments);
        addHackernewsResultInfo(this);
        return result;
    };

})(jQuery);