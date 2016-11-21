// handle ajax errors globally
$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
  console.log('ajax error: ' + thrownError);
  if (jqxhr.status == 403) {
    window.location = '/';
  }
});

var service = {
  calendarCache: {},
  getCalendar: function(year, month, timeZone, responseHandler){
    var cacheKey = '' + year + month;
    if (service.calendarCache[cacheKey]) {
      console.log('returning cached calendar response for ' + cacheKey);
      responseHandler(service.calendarCache[cacheKey]);
      return;
    }
    var uri = window.location + '/calendar';
    $.get(uri, {timeZone:timeZone, year:year, month:month})
      .done(function(data, textStatus, jqXHR){
        service.calendarCache[''+data.year+data.month] = data; // cache the calendar data
        responseHandler(data);
      });
  }
};

var labels = {
  months: {
    1: 'January',
    2: 'February',
    3: 'March',
    4: 'April',
    5: 'May',
    6: 'June',
    7: 'July',
    8: 'August',
    9: 'September',
    10: 'October',
    11: 'November',
    12: 'December'
  }
};

var beanlet = {
  timeZone: '',
  calendar: null,
  goToPreviousMonth: function () {
    if (!beanlet.calendar) return false;
    service.getCalendar(beanlet.calendar.previousYear, beanlet.calendar.previousMonth, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    return false;
  },
  goToNextMonth: function () {
    if (!beanlet.calendar) return false;
    service.getCalendar(beanlet.calendar.nextYear, beanlet.calendar.nextMonth, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    return false;
  },
  initializeCalendarLinks: function () {
    $('#cal-prev').click(beanlet.goToPreviousMonth);
    $('#cal-next').click(beanlet.goToNextMonth);
  },
  initializeCalendar: function () {
    beanlet.timeZone = jstz.determine().name();
    service.getCalendar(null, null, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    beanlet.initializeCalendarLinks();
  },
  getCalendarResponseHandler: function(beanletCalendar) {
    beanlet.calendar = beanletCalendar;
    $('#label-month').text(labels.months[beanletCalendar.month]);
    $('#label-year').text(beanletCalendar.year);
    var days = beanletCalendar.days;
    var day;
    for (var i=0; i < days.length; i++) {
      day = days[i];
      var cell = $('#d'+i).text(day.dayOfMonth).attr('class', day.currentMonth ? '' : 'not-current');
      if (day.today) cell.addClass('today');
      if (day.beanCount) cell.addClass('bg-success');
    }
  },
  initializePage: function () {
    beanlet.initializeCalendar();
  }
};

$(function () {
  console.log('initializing beanlet page');
  beanlet.initializePage();
});