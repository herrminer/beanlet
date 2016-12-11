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
    ajax.post({
      url:"/countIt",
      data:{beanletId:beanletId}
    }, responseHandler);
  },
  addBeanlet: function(name, responseHandler){
    ajax.post({
      url:'/beanlets',
      data:{name:name}
    }, responseHandler);
  },
  sortBeanlets: function(sortBy, responseHandler){
    ajax.post({
      url:'/beanlets/sort',
      data:{sortBy:sortBy}
    }, responseHandler);
  }
};

var beanlets = {
  initializePage: function(){
    beanlets.initializeBeanlets();
    beanlets.initializeAddBeanletModal();
    beanlets.initializeSortModal();
  },
  initializeBeanlets: function(){
    $('.new').each(function(){
      var beanletId = $(this).attr('id');
      $('#ci-'+beanletId).click(function(){beanlets.countIt(beanletId); return false;});
    }).removeClass('new');
  },
  initializeAddBeanletModal: function(){
    $('#modal-add')
      .on('show.bs.modal', function(){ $('#beanlet-name').val(''); })
      .on('shown.bs.modal', function(){ $('#beanlet-name').focus(); });
    $('#btn-add-beanlet').click(beanlets.addBeanlet);
  },
  initializeSortModal: function(){
    $('button[sort-by]').click(beanlets.sortBeanlets);
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
    $('#modal-add').modal('hide');
    newRows.hide().prependTo($('#beanlets')).show('slide', {direction:'up'}, 250);
    beanlets.initializeBeanlets();
  },
  sortBeanlets: function(){
    service.sortBeanlets($(this).attr('sort-by'), beanlets.sortBeanletsResponseHandler);
  },
  sortBeanletsResponseHandler: function (ids) {
    var modal = $('#modal-sort');
    var ul = $('#beanlets');
    // hide the list
    ul.hide();
    // re-order the beanlets
    $.each(ids, function (i, id) {
      ul.append($('#'+id));
    });
    // hide the modal then unveil the reordered list
    modal.on('hidden.bs.modal', function (e) {
      ul.show('slide', {direction:'up'}, 250);
      modal.off('hidden.bs.modal');
    });
    modal.modal('hide');
  }
};

// run when the DOM is ready
$(function(){
  beanlets.initializePage();
});
