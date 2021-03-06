package com.qianzhui.enode.common.scheduling;

import com.qianzhui.enode.common.function.Action;
import com.qianzhui.enode.common.logging.ENodeLogger;
import org.slf4j.Logger;

/**
 * Created by junbo_xu on 2016/3/12.
 */
public class Worker {
    private static final Logger _logger = ENodeLogger.getLog();

    private Object _lockObject = new Object();
    private String _actionName;
    private Action _action;
    private Status _status;

    public String actionName() {
        return _actionName;
    }

    public Worker(String actionName, Action action) {
        _actionName = actionName;
        _action = action;
        _status = Status.Initial;
    }

    public Worker start() {
        synchronized (_lockObject) {
            if (_status.equals(Status.Running)) return this;

            _status = Status.Running;

            Thread thread = new Thread(this::loop, String.format("%s.Worker", _actionName));

            thread.setDaemon(true);

            thread.start();

            return this;
        }
    }

    public Worker stop() {
        synchronized (_lockObject) {
            if (_status.equals(Status.StopRequested)) return this;

            _status = Status.StopRequested;

            return this;
        }
    }

    private void loop() {
        while (this._status.equals(Status.Running)) {
            if (Thread.interrupted()) {
                _logger.info("Worker thread caught ThreadAbortException, try to resetting, actionName:{}", _actionName);
                _logger.info("Worker thread ThreadAbortException resetted, actionName:{}", _actionName);
            }
            try {
                _action.apply();
            } catch (Exception ex) {
                _logger.error(String.format("Worker thread has exception, actionName:%s", _actionName), ex);
            }
        }
    }

    enum Status {
        Initial,
        Running,
        StopRequested
    }
}
