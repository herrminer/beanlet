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
  getBeans: function (dateKey, responseHandler) {
    var uri = window.location + '/beans';
    $.get(uri, {dateKey:dateKey})
      .done(function(data, textStatus, jqXHR){
        responseHandler(data);
      });
  },
  addBean: function (beanletId, dateKey, responseHandler) {
    var uri = window.location + '/beans';
    var csrfToken = $("meta[name='_csrf']").attr("content");
    $.post(uri, {_csrf:csrfToken, dateKey:dateKey})
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
    beanlet.hideBeans(function () {
      service.getCalendar(beanlet.calendar.previousYear, beanlet.calendar.previousMonth, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    });
    return false;
  },
  goToCurrentMonth: function () {
    var currentDate = new Date();
    beanlet.hideBeans(function () {
      service.getCalendar(currentDate.getFullYear(), currentDate.getMonth()+1, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    });
    return false;
  },
  goToNextMonth: function () {
    if (!beanlet.calendar) return false;
    beanlet.hideBeans(function () {
      service.getCalendar(beanlet.calendar.nextYear, beanlet.calendar.nextMonth, beanlet.timeZone, beanlet.getCalendarResponseHandler);
    });
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
      beanletCalendar[day.dateKey] = day; // add another attribute for quick access to a day given its dateKey...todo: move this to service call?
      var cell = $('#d'+i).text(day.dayOfMonth).attr('dateKey', day.dateKey).attr('class', day.currentMonth ? '' : 'not-current');
      if (day.today) cell.addClass('today').addClass('selected');
      if (day.beanCount) cell.addClass('bg-success');
    }
    $('.today').click();
  },
  getBeans: function () {
    $('.selected').removeClass('selected');
    var cell = this;
    var callback = function () {
      var dayIndex = $(cell).addClass('selected').attr('id').substring(1);
      var day = beanlet.calendar.days[dayIndex];
      var dateKey = day.dateKey;
      service.getBeans(dateKey, beanlet.getBeansResponseHandler);
    };
    beanlet.hideBeans(callback);
  },
  hideBeans: function (callback) {
    $('#beans').hide('slide', {direction:'up'}, 100, function(){
      $(this).find('li').remove();
      if (typeof callback == 'function') callback();
    });
  },
  displayBeanModal: function () {
    $('#modal-bean').modal();
    $('#bean-date').datepicker();
  },
  getBeansResponseHandler: function (html) {
    var lis = $(html).find('li').click(beanlet.displayBeanModal);
    $('#beans').append(lis).show('slide', {direction:'up'}, 100);
  },
  addBean: function () {
    var dayIndex = $('.selected').attr('id').substring(1);
    var day = beanlet.calendar.days[dayIndex];
    var dateKey = day.dateKey;
    var beanletId = $('#beanletId').val();
    service.addBean(beanletId, dateKey, beanlet.addBeanResponseHandler);
    return false;
  },
  /**
   * @param addBeanResponse
   * - beanletId
   * - beanCountForDate
   * - dateKey
   */
  addBeanResponseHandler: function (beanChangeResponse) {
    var dateCell = $('[dateKey='+beanChangeResponse.dateKey+']');
    beanlet.calendar[beanChangeResponse.dateKey].beanCount = beanChangeResponse.beanCountForDate; // update the cached count of beans for the day
    if (beanChangeResponse.beanCountForDate) {
      dateCell.addClass('bg-success');
    } else {
      dateCell.removeClass('bg-success');
    }
    dateCell.click();
  },
  initializeFooterButtons: function () {
    $('#add-beanlet').click(beanlet.addBean);
  },
  initializePage: function () {
    beanlet.initializeCalendar();
    beanlet.initializeFooterButtons();
  }
};

$(function () {
  console.log('initializing beanlet page');
  beanlet.initializePage();
});