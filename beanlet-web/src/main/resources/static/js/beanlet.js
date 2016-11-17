var service = {

};

var beanlet = {
  initializePage: function () {
    for (var i=0; i < 31; i++) {
      $('#d'+i).text(i+1);
    }
    $('#beans-table').find('td').each(function(i, it){
      var jq = $(it);
      jq.click(function(){$(this).toggleClass('bg-success', 250)});
      if (i % 3 == 0 && i < 31) {
          jq.addClass('bg-success');
      }
    });
  }
};

$(function () {
  console.log('initializing beanlet page');
  beanlet.initializePage();
});