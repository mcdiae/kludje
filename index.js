
(function () {
    var prettify, counter = 0;

    $("[id]").each(function (idx, element) {
        var anchor = $("<a>&#167;</a>").attr("href", "#" + element.id)[0];
        $(element).append(anchor);
    });

    prettify = function () {
        if(counter === 0) {
            $("head").append("<script src='http://google-code-prettify.googlecode.com/svn/loader/run_prettify.js'></script>");
        }
    };

    $("[data-src]").each(function (idx, element) {
        counter++;
        $.ajax({
              dataType: "text",
              url: $(element).attr("data-src"),
              success: function (data) {
                $(element).text(data);
                counter--;
                prettify();
              }
        });
    });
} ());
