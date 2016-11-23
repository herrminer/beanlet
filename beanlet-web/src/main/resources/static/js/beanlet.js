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
  },
  getBeans: function (date, responseHandler) {
    var uri = window.location + '/beans';
    $.get(uri, {date:date})
      .done(function(data, textStatus, jqXHR){
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
  goToCurrentMonth: function () {
    var currentDate = new Date();
    service.getCalendar(currentDate.getFullYear(), currentDate.getMonth()+1, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    return false;
  },
  goToNextMonth: function () {
    if (!beanlet.calendar) return false;
    service.getCalendar(beanlet.calendar.nextYear, beanlet.calendar.nextMonth, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    return false;
  },
  initializeCalendarLinks: function () {
    $('#cal-prev').click(beanlet.goToPreviousMonth);
    $('#cal-today').click(beanlet.goToCurrentMonth);
    $('#cal-next').click(beanlet.goToNextMonth);
  },
  initializeCalendar: function () {
    beanlet.timeZone = jstz.determine().name();
    beanlet.initializeCalendarLinks();
    $('#beans-table').find('td').click(beanlet.getBeans);
    service.getCalendar(null, null, beanlet.timeZone, beanlet.getCalendarResponseHandler);
  },
  showWeeks: function (numDays) {
    var weeksToShow = numDays / 7;
    for (var i = 0; i < weeksToShow; i++) {
      $('#week'+i).show();
    }
    for (var j = 5; j >= weeksToShow; j--) {
      $('#week'+j).hide();
    }
  },
  getCalendarResponseHandler: function(beanletCalendar) {
    beanlet.calendar = beanletCalendar;
    $('#label-month').text(labels.months[beanletCalendar.month]);
    $('#label-year').text(beanletCalendar.year);
    var days = beanletCalendar.days;
    beanlet.showWeeks(days.length);
    var day;
    for (var i=0; i < days.length; i++) {
      day = days[i];
      var cell = $('#d'+i).text(day.dayOfMonth).attr('class', day.currentMonth ? '' : 'not-current');
      if (day.today) cell.addClass('today').addClass('selected');
      if (day.beanCount) cell.addClass('bg-success');
    }
  },
  getBeans: function () {
    $('#beans').hide().find('li').remove();
    $('.selected').removeClass('selected');
    var dayIndex = $(this).addClass('selected').attr('id').substring(1);
    var day = beanlet.calendar.days[dayIndex];
    var date = [day.year, day.month, day.dayOfMonth].join('-');
    service.getBeans(date, beanlet.getBeansResponseHandler);
  },
  getBeansResponseHandler: function (html) {
    $('#beans').append($(html).find('li')).show();
  },
  initializePage: function () {
    beanlet.initializeCalendar();
  }
};

$(function () {
  console.log('initializing beanlet page');
  beanlet.initializePage();
});