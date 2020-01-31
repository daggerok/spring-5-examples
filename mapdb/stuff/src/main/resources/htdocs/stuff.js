function registerContentClick() {
  $("div.content>a").click(function () {
    var link = this;
    $.ajax({
      type: "GET",
      url: this.getAttribute("href"),
      dataType: "html",
      success: function (html, status) {
        link.parentNode.innerHTML = $(html).next("div.content").html()
        registerContentClick()
      },
      error: function (xhr, textStatus, errorThrown) {
        alert("An error occurred")
      }
    })
    return false;
  })
}

jQuery(document).ready(function () {
  registerContentClick()

  $("div.component>a").each(function (component) {
    $(this.parentNode).load(this.getAttribute("href"))
  })


})
