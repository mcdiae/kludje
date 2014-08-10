
(function () {
    var prettify, counter = 0;

    $("[id]").each(function (idx, element) {
        var anchor, el, li;
        el = $(element);

        anchor = $("<a />").text(el.text())
            .attr("href", "#" + element.id);
        li = $("<li />").append(anchor);
        $("ul[data-tag='menu']").append(li);

        anchor = $("<a>&#167;</a>").attr("href", "#" + element.id)
            .attr("title", el.text())[0];
        el.append(anchor);
    });

    $("a").attr("target", "_top");

    prettify = function () {
        if(counter === 0) {
            $("head").append("<script src='http://google-code-prettify.googlecode.com/svn/loader/run_prettify.js'></script>");
        }
    };

    $("[data-src]").each(function (idx, element) {
        $(element).text("loading " + $(element).attr("data-src") + "...");
        counter++;
        $.ajax({
              dataType: "text",
              url: $(element).attr("data-src"),
              success: function (data) {
                if(/^\/\*/.test(data)) {
                  data = data.slice(data.indexOf("*/") + 2).trim();
                }

                $(element).text(data);
                counter--;
                prettify();
              }
        });
    });
} ());
