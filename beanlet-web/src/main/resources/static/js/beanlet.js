var service = {

};

var beanlet = {
  initializePage: function () {
    for (var i=0; i < 31; i++) {
      $('#d'+i).text(i+1);
    }
    $('#beans-table').find('td').each(function(i, it){
      if (i % 3 == 0 && i < 31)
        $(it).addClass('bg-success');
    });
  }
};

$(function () {
  console.log('initializing beanlet page');
  beanlet.initializePage();
});