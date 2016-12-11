$(document).ajaxStart(function () {
  $("#loading").show();
});
$(document).ajaxStop(function () {
  $("#loading").hide();
});
var ajax = {
  get: function (options, responseHandler) {
    ajax.doWithMethod('GET', options, responseHandler);
  },
  put: function (options, responseHandler) {
    options.addCsrfToken = true;
    ajax.doWithMethod('PUT', options, responseHandler);
  },
  post: function (options, responseHandler) {
    options.addCsrfToken = true;
    ajax.doWithMethod('POST', options, responseHandler);
  },
  delete: function (options, responseHandler) {
    options.addCsrfToken = true;
    ajax.doWithMethod('DELETE', options, responseHandler);
  },
  doWithMethod: function(method, options, responseHandler) {
    options.type = method;
    ajax.addTimeZoneHeader(options);
    if (options.addCsrfToken) {
      ajax.addCsrfTokenHeader(options);
    }
    $.ajax(options).done(responseHandler);
  },
  addCsrfTokenHeader: function (options) {
    var csrfToken = $("meta[name='_csrf']").attr("content");
    if (!csrfToken) { logger.debug('no csrf token found in meta tag'); return false; }
    options.headers["X-CSRF-TOKEN"] = csrfToken;
  },
  addTimeZoneHeader: function (options) {
    if (!options.headers) { options.headers = {}; }
    if (!ajax.timeZone) {
      ajax.timeZone = jstz.determine().name();
    }
    options.headers["X-BNLT-TIMEZONE"] = ajax.timeZone;
  }
};
