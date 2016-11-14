// handle ajax errors globally
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  console.log('ajax error: ' + thrownError);
  if (jqxhr.status == 403) {
    window.location = '/';
  }
});

var service = {
  countIt: function(beanletId, responseHandler){
    if (typeof responseHandler != 'function') {
      throw new Error('successHandler not a function: ' + responseHandler);
    }
    var timeZone = jstz.determine().name();
    var csrfToken = $("meta[name='_csrf']").attr("content");
    $.post("/countIt", {beanletId:beanletId, _csrf:csrfToken, timeZone: timeZone})
      .done(function(data, textStatus, jqXHR){
        responseHandler(data);
      });
  },
  addBeanlet: function(name, responseHandler){
    var token = $("meta[name='_csrf']").attr("content");
    $.post("/beanlets", {_csrf:token, name:name})
      .done(function(data, textStatus, jqXHR){
        responseHandler(data);
      });
  }
};

var beanlets = {
  initializePage: function(){
    beanlets.initializeBeanlets();
    beanlets.initializeAddBeanletForm();
  },
  initializeAddBeanletForm: function(){
    $('#modal-add').on('shown.bs.modal', function(){
      $('#beanlet-name').focus()
    });
    $('#btn-add-beanlet').click(beanlets.addBeanlet);
  },
  initializeBeanlets: function(){
    $('.new').each(function(){
      var beanletId = $(this).attr('id');
      $('#ci-'+beanletId).click(function(){beanlets.countIt(beanletId); return false;});
    }).removeClass('new');
  },
  countIt: function(beanletId) {
    $('#count-'+beanletId).text('').addClass('loading');
    service.countIt(beanletId, beanlets.countItResponseHandler);
  },
  countItResponseHandler: function(countItResponse) {
    var beanletId = countItResponse.beanletId;
    $('#ll-' + beanletId).text(countItResponse.lastLogged);
    $('#count-' + beanletId).removeClass('loading').text(countItResponse.beanCount);
    var li = $('#' + beanletId);
    li.addClass('bg-success', 250);
    setTimeout(function () {
      li.removeClass('bg-success', 500)
    }, 2000);
  },
  addBeanlet: function(){
    var name = $('#beanlet-name').val();
    service.addBeanlet(name, beanlets.addBeanletResponseHandler);
  },
  addBeanletResponseHandler: function(response){
    var newRows = $(response).find('li.beanlet');
    newRows.prependTo($('#beanlets'));
    beanlets.initializeBeanlets();
    $('#modal-add').modal('hide');
  }
};

// run when the DOM is ready
$(function(){
  beanlets.initializePage();
});
