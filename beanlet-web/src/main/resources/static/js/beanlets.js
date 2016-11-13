// handle ajax errors globally
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  console.log('ajax error: ' + thrownError);
  if (jqxhr.status == 403) {
    window.location = '/';
  }
});

// function to count a bean
var service = {
  countIt: function(beanletId, responseHandler){
    if (typeof responseHandler != 'function') {
      throw new Error('successHandler not a function: ' + responseHandler);
    }
    console.log('service.countIt ' + beanletId);
    var timeZone = jstz.determine().name();
    var csrfToken = $("meta[name='_csrf']").attr("content");
    $.post("/countIt", {beanletId:beanletId, _csrf:csrfToken, timeZone: timeZone})
      .done(function(data, textStatus, jqXHR){
        responseHandler(data);
      });
  }
};

var beanlets = {
  countIt: function(beanletId) {
    $('#'+beanletId).addClass('bg-warning');
    service.countIt(beanletId, beanlets.countItSuccess);
  },
  countItSuccess: function(countItResponse){
    var beanletId = countItResponse.beanletId;
    $('#ll-'+beanletId).text(countItResponse.lastLogged);
    $('#count-'+beanletId).text(countItResponse.beanCount);
    var li = $('#'+beanletId);
    var fadeOut = function(){ setTimeout(function(){ li.removeClass('bg-success', 1000) }, 2000) };
    li.switchClass('bg-warning', 'bg-success', 1000, 'linear', fadeOut);
  }
};

// function to initialize beanlets with appropriate event handlers
var initializeBeanlets = function(){
  $('.new').each(function(){
    var beanletId = $(this).attr('id');
    $('#ci-'+beanletId).click(function(){beanlets.countIt(beanletId); return false;});
  }).removeClass('new');
};

// run when the DOM is ready
$(function(){
  initializeBeanlets();
  $('#btn-add-beanlet').click(function(){
    var token = $("meta[name='_csrf']").attr("content");
    var name = $('#beanlet-name').val();
    $.post("/beanlets", {_csrf:token, name:name})
      .done(function(data, textStatus, jqXHR){
        var beanlets = $(data).find('div.beanlet');
        beanlets.appendTo($('#beanlet-list'));
        initializeBeanlets();
      })
      .fail(function(jqXHR, textStatus, errorThrown){
        console.log(jqXHR.responseText);
        // detect if the session has expired
        if (jqXHR.status == 403) {
          window.location = '/';
        }
      });
  });
});
