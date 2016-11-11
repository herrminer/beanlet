// handle ajax errors globally
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  console.log('ajax error: ' + thrownError);
  if (jqxhr.status == 403) {
    window.location = '/';
  }
});

// function to count a bean
var countIt = function(beanletId){
  console.log('counting ' + beanletId);
  var timeZone = jstz.determine().name();
  var csrfToken = $("meta[name='_csrf']").attr("content");
  $.post("/countIt", {beanletId:beanletId, _csrf:csrfToken, timeZone: timeZone})
    .done(function(data, textStatus, jqXHR){
      $('#last-' + beanletId).text(data);
    });
};

// function to move a beanlet up in the list
var moveUp = function(){ console.log('moving up') };

// function to move a beanlet down in the list
var moveDown = function(){ console.log('moving down') };

// function to initialize beanlets with appropriate event handlers
var initializeBeanlets = function(){
  $('div.beanlet.new').each(function(){
    var beanlet = $(this);
    var beanletId = beanlet.attr('id');
    $('#ci-'+beanletId).attr('bid', beanletId).click(function(){countIt(beanletId);});
    $('#mu-'+beanletId).attr('bid', beanletId).click(moveUp);
    $('#md-'+beanletId).attr('bid', beanletId).click(moveDown);
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
