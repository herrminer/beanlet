// handle ajax errors globally
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  console.log('ajax error: ' + thrownError);
  if (jqxhr.status == 403) {
    window.location = '/';
  }
});

var service = {
  getCalendar: function(year, month, timeZone, responseHandler){
    $.get("/beanlets/12345/calendar", {timeZone:timeZone})
      .done(function(data, textStatus, jqXHR){
        responseHandler(data);
      });
  }
};

var beanlet = {
  initializeCalendar: function () {
    var timeZone = jstz.determine().name();
    service.getCalendar(null, null, timeZone, beanlet.getCalendarResponseHandler);
  },
  getCalendarResponseHandler: function(beanletCalendar) {
    var days = beanletCalendar.days;
    var day;
    for (var i=0; i < days.length; i++) {
      day = days[i];
      $('#d'+i)
        .text(day.dayOfMonth)
        .attr('class', day.currentMonth ? '' : 'not-current');
    }
  },
  initializePage: function () {
    beanlet.initializeCalendar();
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