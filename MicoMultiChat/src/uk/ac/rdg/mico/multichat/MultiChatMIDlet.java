

package uk.ac.rdg.mico.multichat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.microedition.media.MediaException;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import javax.microedition.media.control.RecordControl;
import javax.microedition.media.control.VideoControl;
import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;
import net.sf.microlog.midp.appender.FormAppender;
import org.netbeans.microedition.lcdui.SplashScreen;
import org.netbeans.microedition.lcdui.WaitScreen;
import org.netbeans.microedition.util.SimpleCancellableTask;
import uk.ac.rdg.acet.mico.Platform;
import uk.ac.rdg.acet.mico.comms.CommsEventListener;
import uk.ac.rdg.acet.mico.comms.CommsService;
import uk.ac.rdg.acet.mico.ident.IdentService;

/**
 * @author David
 */
public class MultiChatMIDlet extends MIDlet implements CommandListener {

    private boolean midletPaused = false;
    private static final Logger log = LoggerFactory.getLogger(MultiChatMIDlet.class);
    private Platform p = null;
    private CommsService comms = null;
    private IdentService ident = null;

    private Player mAudioPlayer = null;
    private RecordControl mAudioRecordControl = null;
    private ByteArrayOutputStream audioOutputStream = new ByteArrayOutputStream();
    private byte[] audioMessageCache = null;

    private Player mVideoPlayer = null;
    private VideoControl mVideoControl = null;
    private RecordControl mVideoRecordControl = null;
    private ByteArrayOutputStream videoOutputStream = null;
    private byte[] videoMessageCache = null;

    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private WaitScreen startupWaitScreen;
    private TextBox selectNameTextBox;
    private List chatLogList;
    private TextBox sendTextTextBox;
    private WaitScreen sendingTextWaitScreen;
    private SplashScreen startupErrorSplashScreen;
    private Form recordAudioForm;
    private Form recordVideoForm;
    private WaitScreen sendingAudioWaitScreen;
    private WaitScreen sendingVideoWaitScreen;
    private Form errorLogForm;
    private Form viewVideoForm;
    private Command okCommand;
    private Command exitCommand;
    private Command sendVideoCommand;
    private Command sendTextCommand;
    private Command backCommand;
    private Command sendAudioCommand;
    private Command cancelCommand;
    private Command playSelectedCommand;
    private SimpleCancellableTask startupMicoTask;
    private SimpleCancellableTask sendingTextTask;
    private SimpleCancellableTask sendingAudioTask;
    private SimpleCancellableTask sendingVideoTask;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The MultiChatMIDlet constructor.
     */
    public MultiChatMIDlet() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getStartupWaitScreen());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == chatLogList) {//GEN-BEGIN:|7-commandAction|1|28-preAction
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|1|28-preAction
                // write pre-action user code here
                chatLogListAction();//GEN-LINE:|7-commandAction|2|28-postAction
                // write post-action user code here
            } else if (command == exitCommand) {//GEN-LINE:|7-commandAction|3|37-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|4|37-postAction
                // write post-action user code here
            } else if (command == playSelectedCommand) {//GEN-LINE:|7-commandAction|5|107-preAction
                // write pre-action user code here
                if ((chatLogList.getString(chatLogList.getSelectedIndex()).indexOf("[video]")>01)&&(videoMessageCache!=null)) {
                    // switch to video canvas to play this
                    ByteArrayInputStream videoInputStream = new ByteArrayInputStream(videoMessageCache);
                    try {
                        mVideoPlayer = Manager.createPlayer(videoInputStream, "video/3gpp");
                        mVideoPlayer.realize();
                        mVideoPlayer.start();
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                    mVideoControl = (VideoControl)mVideoPlayer.getControl("VideoControl");
                    switchDisplayable(null, getViewVideoForm());//GEN-LINE:|7-commandAction|6|107-postAction
                // write post-action user code here
                } else
                if ((chatLogList.getString(chatLogList.getSelectedIndex()).indexOf("[audio]")>-1)&&(audioMessageCache!=null)) {
                    ByteArrayInputStream audioInputStream = new ByteArrayInputStream(audioMessageCache);
                    try {
                        mAudioPlayer = Manager.createPlayer(audioInputStream, "audio/wav");
                        mAudioPlayer.start();
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                    audioMessageCache = null;
                }
            } else if (command == sendAudioCommand) {//GEN-LINE:|7-commandAction|7|45-preAction
                // write pre-action user code here
                switchDisplayable(null, getRecordAudioForm());//GEN-LINE:|7-commandAction|8|45-postAction
                // write post-action user code here
                audioOutputStream.reset();
                mAudioRecordControl.startRecord();
                try {
                    mAudioPlayer.start();
                } catch (MediaException ex) {
                    log.error(ex);
                }
            } else if (command == sendTextCommand) {//GEN-LINE:|7-commandAction|9|41-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendTextTextBox());//GEN-LINE:|7-commandAction|10|41-postAction
                // write post-action user code here
            } else if (command == sendVideoCommand) {//GEN-LINE:|7-commandAction|11|43-preAction
                // write pre-action user code here
                // setup video recorder
                mVideoPlayer = null;
                mVideoControl = null;
                mVideoRecordControl = null;
                videoOutputStream = null;
                try {
                    mVideoPlayer = Manager.createPlayer("capture://devcam1?encoding=video/3gpp"); // try create front cam
                    if (mVideoPlayer==null) {
                        mVideoPlayer = Manager.createPlayer("capture://video?encoding=video/3gpp"); // create back cam if front not available
                    }
                    mVideoPlayer.realize();
                    mVideoControl = (VideoControl)mVideoPlayer.getControl("VideoControl");
                    mVideoRecordControl = (RecordControl)mVideoPlayer.getControl("RecordControl");
                    videoOutputStream = new ByteArrayOutputStream();
                    mVideoRecordControl.setRecordStream(videoOutputStream);
                } catch (Exception ex) {
                    log.error(ex);
                }
                switchDisplayable(null, getRecordVideoForm());//GEN-LINE:|7-commandAction|12|43-postAction
                try {
                    // write post-action user code here
                    mVideoPlayer.start();
                } catch (MediaException ex) {
                    log.error(ex);
                }
                mVideoRecordControl.startRecord();
            }//GEN-BEGIN:|7-commandAction|13|102-preAction
        } else if (displayable == errorLogForm) {
            if (command == exitCommand) {//GEN-END:|7-commandAction|13|102-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|14|102-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|15|77-preAction
        } else if (displayable == recordAudioForm) {
            if (command == cancelCommand) {//GEN-END:|7-commandAction|15|77-preAction
                // write pre-action user code here
                mVideoRecordControl.stopRecord();
                try {
                    mVideoPlayer.stop();
                } catch (MediaException ex) {
                    log.error(ex);
                }
                videoOutputStream.reset();
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|16|77-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|17|80-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendingAudioWaitScreen());//GEN-LINE:|7-commandAction|18|80-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|19|82-preAction
        } else if (displayable == recordVideoForm) {
            if (command == cancelCommand) {//GEN-END:|7-commandAction|19|82-preAction
                // write pre-action user code here
                mVideoRecordControl.stopRecord();
                try {
                    mVideoPlayer.stop();
                } catch (MediaException ex) {
                    log.error(ex);
                }
                videoOutputStream.reset();
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|20|82-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|21|81-preAction
                // write pre-action user code here

                switchDisplayable(null, getSendingVideoWaitScreen());//GEN-LINE:|7-commandAction|22|81-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|23|32-preAction
        } else if (displayable == selectNameTextBox) {
            if (command == exitCommand) {//GEN-END:|7-commandAction|23|32-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|24|32-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|25|23-preAction
                ident.setLocalFriendlyName(selectNameTextBox.getString());
                // write pre-action user code here
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|26|23-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|27|48-preAction
        } else if (displayable == sendTextTextBox) {
            if (command == backCommand) {//GEN-END:|7-commandAction|27|48-preAction
                // write pre-action user code here
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|28|48-postAction
                // write post-action user code here
            } else if (command == okCommand) {//GEN-LINE:|7-commandAction|29|49-preAction
                // write pre-action user code here
                switchDisplayable(null, getSendingTextWaitScreen());//GEN-LINE:|7-commandAction|30|49-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|31|87-preAction
        } else if (displayable == sendingAudioWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|31|87-preAction
                // write pre-action user code here
                switchDisplayable(null, getErrorLogForm());//GEN-LINE:|7-commandAction|32|87-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|33|86-preAction
                // write pre-action user code here
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|34|86-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|35|54-preAction
        } else if (displayable == sendingTextWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|35|54-preAction
                // write pre-action user code here
                switchDisplayable(null, getErrorLogForm());//GEN-LINE:|7-commandAction|36|54-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|37|53-preAction
                // write pre-action user code here
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|38|53-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|39|95-preAction
        } else if (displayable == sendingVideoWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|39|95-preAction
                // write pre-action user code here
                switchDisplayable(null, getErrorLogForm());//GEN-LINE:|7-commandAction|40|95-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|41|94-preAction
                // write pre-action user code here
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|42|94-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|43|65-preAction
        } else if (displayable == startupErrorSplashScreen) {
            if (command == SplashScreen.DISMISS_COMMAND) {//GEN-END:|7-commandAction|43|65-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|44|65-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|45|18-preAction
        } else if (displayable == startupWaitScreen) {
            if (command == WaitScreen.FAILURE_COMMAND) {//GEN-END:|7-commandAction|45|18-preAction
                // write pre-action user code here
                switchDisplayable(null, getStartupErrorSplashScreen());//GEN-LINE:|7-commandAction|46|18-postAction
                // write post-action user code here
            } else if (command == WaitScreen.SUCCESS_COMMAND) {//GEN-LINE:|7-commandAction|47|17-preAction
                // write pre-action user code here
                switchDisplayable(null, getSelectNameTextBox());//GEN-LINE:|7-commandAction|48|17-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|49|109-preAction
        } else if (displayable == viewVideoForm) {
            if (command == okCommand) {//GEN-END:|7-commandAction|49|109-preAction
                // write pre-action user code here
                try {
                    mVideoPlayer.stop();
                } catch (MediaException ex) {
                    ex.printStackTrace();
                }
                mVideoPlayer.close();
                videoMessageCache = null;
                switchDisplayable(null, getChatLogList());//GEN-LINE:|7-commandAction|50|109-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|51|7-postCommandAction
        }//GEN-END:|7-commandAction|51|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|52|
    //</editor-fold>//GEN-END:|7-commandAction|52|


    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: startupWaitScreen ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initiliazed instance of startupWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getStartupWaitScreen() {
        if (startupWaitScreen == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            startupWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|14-getter|1|14-postInit
            startupWaitScreen.setTitle("Starting Mico");
            startupWaitScreen.setCommandListener(this);
            startupWaitScreen.setTask(getStartupMicoTask());//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return startupWaitScreen;
    }
    //</editor-fold>//GEN-END:|14-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: selectNameTextBox ">//GEN-BEGIN:|21-getter|0|21-preInit
    /**
     * Returns an initiliazed instance of selectNameTextBox component.
     * @return the initialized component instance
     */
    public TextBox getSelectNameTextBox() {
        if (selectNameTextBox == null) {//GEN-END:|21-getter|0|21-preInit
            // write pre-init user code here
            selectNameTextBox = new TextBox("Enter a friendly name", ident.getLocalFriendlyName(), 100, TextField.ANY);//GEN-BEGIN:|21-getter|1|21-postInit
            selectNameTextBox.addCommand(getOkCommand());
            selectNameTextBox.addCommand(getExitCommand());
            selectNameTextBox.setCommandListener(this);//GEN-END:|21-getter|1|21-postInit
            // write post-init user code here
        }//GEN-BEGIN:|21-getter|2|
        return selectNameTextBox;
    }
    //</editor-fold>//GEN-END:|21-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: chatLogList ">//GEN-BEGIN:|26-getter|0|26-preInit
    /**
     * Returns an initiliazed instance of chatLogList component.
     * @return the initialized component instance
     */
    public List getChatLogList() {
        if (chatLogList == null) {//GEN-END:|26-getter|0|26-preInit
            // write pre-init user code here
            chatLogList = new List("Mico MultiChat", Choice.IMPLICIT);//GEN-BEGIN:|26-getter|1|26-postInit
            chatLogList.addCommand(getSendTextCommand());
            chatLogList.addCommand(getSendAudioCommand());
            chatLogList.addCommand(getSendVideoCommand());
            chatLogList.addCommand(getPlaySelectedCommand());
            chatLogList.addCommand(getExitCommand());
            chatLogList.setCommandListener(this);
            chatLogList.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
            chatLogList.setSelectCommand(getPlaySelectedCommand());//GEN-END:|26-getter|1|26-postInit
            // write post-init user code here
        }//GEN-BEGIN:|26-getter|2|
        return chatLogList;
    }
    //</editor-fold>//GEN-END:|26-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: chatLogListAction ">//GEN-BEGIN:|26-action|0|26-preAction
    /**
     * Performs an action assigned to the selected list element in the chatLogList component.
     */
    public void chatLogListAction() {//GEN-END:|26-action|0|26-preAction
        // enter pre-action user code here
        String __selectedString = getChatLogList().getString(getChatLogList().getSelectedIndex());//GEN-LINE:|26-action|1|26-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|26-action|2|
    //</editor-fold>//GEN-END:|26-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: okCommand ">//GEN-BEGIN:|22-getter|0|22-preInit
    /**
     * Returns an initiliazed instance of okCommand component.
     * @return the initialized component instance
     */
    public Command getOkCommand() {
        if (okCommand == null) {//GEN-END:|22-getter|0|22-preInit
            // write pre-init user code here
            okCommand = new Command("Ok", Command.OK, 0);//GEN-LINE:|22-getter|1|22-postInit
            // write post-init user code here
        }//GEN-BEGIN:|22-getter|2|
        return okCommand;
    }
    //</editor-fold>//GEN-END:|22-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommand ">//GEN-BEGIN:|31-getter|0|31-preInit
    /**
     * Returns an initiliazed instance of exitCommand component.
     * @return the initialized component instance
     */
    public Command getExitCommand() {
        if (exitCommand == null) {//GEN-END:|31-getter|0|31-preInit
            // write pre-init user code here
            exitCommand = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|31-getter|1|31-postInit
            // write post-init user code here
        }//GEN-BEGIN:|31-getter|2|
        return exitCommand;
    }
    //</editor-fold>//GEN-END:|31-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: startupMicoTask ">//GEN-BEGIN:|39-getter|0|39-preInit
    /**
     * Returns an initiliazed instance of startupMicoTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getStartupMicoTask() {
        if (startupMicoTask == null) {//GEN-END:|39-getter|0|39-preInit
            // write pre-init user code here
            startupMicoTask = new SimpleCancellableTask();//GEN-BEGIN:|39-getter|1|39-execute
            startupMicoTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|39-getter|1|39-execute
                    // write task-execution user code here
                    log.addAppender(new FormAppender(getErrorLogForm()));
                    // setup Mico Platform
                    Platform.setSuperPeerURL("samson.acet.reading.ac.uk");
                    Platform.setPeerName("default_peer");
                    p = Platform.getInstance();
                    try {
                        p.startNetwork();
                    } catch (Exception e) {
                        log.fatal(e);
                    }
                    // setup Mico Identity Service
                    ident = (IdentService)p.getService(IdentService.class.getName());
                    ident.setLocalFriendlyName("DefaultName");
                    // setup Mico Communications Service
                    comms = (CommsService)p.getService(CommsService.class.getName());
                    comms.addEventListener(new CommsEventListener() {
                        IdentService identService = (IdentService)p.getService(IdentService.class.getName());
                        public void textMessageReceived(String sender, String textString) {
                            String senderName = identService.getRemoteFriendlyName(sender);
                            chatLogList.insert(0, senderName + ": " + textString, null);
//                            AlertType.INFO.playSound(MultiChatMIDlet.this.getDisplay());
//                            MultiChatMIDlet.this.getDisplay().vibrate(500);
                        }

                        public void audioMessageReceived(String sender, byte[] audioData) {
                            String senderName = identService.getRemoteFriendlyName(sender);
                            chatLogList.insert(0, senderName + ": " + "[audio] " + audioData.length + "b", null);
                            audioMessageCache = audioData;
//                            AlertType.INFO.playSound(MultiChatMIDlet.this.getDisplay());
//                            MultiChatMIDlet.this.getDisplay().vibrate(500);
                        }

                        public void videoMessageReceived(String sender, byte[] videoData) {
                            String senderName = identService.getRemoteFriendlyName(sender);
                            chatLogList.insert(0, senderName + ": " + "[video] " + videoData.length + "b", null);
                            videoMessageCache = videoData;
//                            AlertType.INFO.playSound(MultiChatMIDlet.this.getDisplay());
//                            MultiChatMIDlet.this.getDisplay().vibrate(500);
                        }
                    });

                    // TODO add checks if video is available at all
                    // setup audio recorder
                    try {
                        mAudioPlayer = Manager.createPlayer("capture://audio");
                        mAudioPlayer.realize();
                        mAudioRecordControl = (RecordControl)mAudioPlayer.getControl("RecordControl");
                        mAudioRecordControl.setRecordStream(audioOutputStream);
                    } catch (Exception ex) {
                        log.error(ex);
                    }
                }//GEN-BEGIN:|39-getter|2|39-postInit
            });//GEN-END:|39-getter|2|39-postInit
            // write post-init user code here
        }//GEN-BEGIN:|39-getter|3|
        return startupMicoTask;
    }
    //</editor-fold>//GEN-END:|39-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendTextTextBox ">//GEN-BEGIN:|46-getter|0|46-preInit
    /**
     * Returns an initiliazed instance of sendTextTextBox component.
     * @return the initialized component instance
     */
    public TextBox getSendTextTextBox() {
        if (sendTextTextBox == null) {//GEN-END:|46-getter|0|46-preInit
            // write pre-init user code here
            sendTextTextBox = new TextBox("Enter text message", null, 100, TextField.ANY);//GEN-BEGIN:|46-getter|1|46-postInit
            sendTextTextBox.addCommand(getOkCommand());
            sendTextTextBox.addCommand(getBackCommand());
            sendTextTextBox.setCommandListener(this);//GEN-END:|46-getter|1|46-postInit
            // write post-init user code here
        }//GEN-BEGIN:|46-getter|2|
        return sendTextTextBox;
    }
    //</editor-fold>//GEN-END:|46-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendingTextWaitScreen ">//GEN-BEGIN:|52-getter|0|52-preInit
    /**
     * Returns an initiliazed instance of sendingTextWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getSendingTextWaitScreen() {
        if (sendingTextWaitScreen == null) {//GEN-END:|52-getter|0|52-preInit
            // write pre-init user code here
            sendingTextWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|52-getter|1|52-postInit
            sendingTextWaitScreen.setTitle("Sending text message");
            sendingTextWaitScreen.setCommandListener(this);
            sendingTextWaitScreen.setTask(getSendingTextTask());//GEN-END:|52-getter|1|52-postInit
            // write post-init user code here
        }//GEN-BEGIN:|52-getter|2|
        return sendingTextWaitScreen;
    }
    //</editor-fold>//GEN-END:|52-getter|2|



    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: startupErrorSplashScreen ">//GEN-BEGIN:|64-getter|0|64-preInit
    /**
     * Returns an initiliazed instance of startupErrorSplashScreen component.
     * @return the initialized component instance
     */
    public SplashScreen getStartupErrorSplashScreen() {
        if (startupErrorSplashScreen == null) {//GEN-END:|64-getter|0|64-preInit
            // write pre-init user code here
            startupErrorSplashScreen = new SplashScreen(getDisplay());//GEN-BEGIN:|64-getter|1|64-postInit
            startupErrorSplashScreen.setTitle("Startup error");
            startupErrorSplashScreen.setCommandListener(this);//GEN-END:|64-getter|1|64-postInit
            // write post-init user code here
        }//GEN-BEGIN:|64-getter|2|
        return startupErrorSplashScreen;
    }
    //</editor-fold>//GEN-END:|64-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendTextCommand ">//GEN-BEGIN:|40-getter|0|40-preInit
    /**
     * Returns an initiliazed instance of sendTextCommand component.
     * @return the initialized component instance
     */
    public Command getSendTextCommand() {
        if (sendTextCommand == null) {//GEN-END:|40-getter|0|40-preInit
            // write pre-init user code here
            sendTextCommand = new Command("Send text", Command.ITEM, 0);//GEN-LINE:|40-getter|1|40-postInit
            // write post-init user code here
        }//GEN-BEGIN:|40-getter|2|
        return sendTextCommand;
    }
    //</editor-fold>//GEN-END:|40-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendVideoCommand ">//GEN-BEGIN:|42-getter|0|42-preInit
    /**
     * Returns an initiliazed instance of sendVideoCommand component.
     * @return the initialized component instance
     */
    public Command getSendVideoCommand() {
        if (sendVideoCommand == null) {//GEN-END:|42-getter|0|42-preInit
            // write pre-init user code here
            sendVideoCommand = new Command("Send video", Command.ITEM, 0);//GEN-LINE:|42-getter|1|42-postInit
            // write post-init user code here
        }//GEN-BEGIN:|42-getter|2|
        return sendVideoCommand;
    }
    //</editor-fold>//GEN-END:|42-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendAudioCommand ">//GEN-BEGIN:|44-getter|0|44-preInit
    /**
     * Returns an initiliazed instance of sendAudioCommand component.
     * @return the initialized component instance
     */
    public Command getSendAudioCommand() {
        if (sendAudioCommand == null) {//GEN-END:|44-getter|0|44-preInit
            // write pre-init user code here
            sendAudioCommand = new Command("Send audio", Command.ITEM, 0);//GEN-LINE:|44-getter|1|44-postInit
            // write post-init user code here
        }//GEN-BEGIN:|44-getter|2|
        return sendAudioCommand;
    }
    //</editor-fold>//GEN-END:|44-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|47-getter|0|47-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|47-getter|0|47-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|47-getter|1|47-postInit
            // write post-init user code here
        }//GEN-BEGIN:|47-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|47-getter|2|
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendingTextTask ">//GEN-BEGIN:|55-getter|0|55-preInit
    /**
     * Returns an initiliazed instance of sendingTextTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getSendingTextTask() {
        if (sendingTextTask == null) {//GEN-END:|55-getter|0|55-preInit
            // write pre-init user code here
            sendingTextTask = new SimpleCancellableTask();//GEN-BEGIN:|55-getter|1|55-execute
            sendingTextTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|55-getter|1|55-execute
                    // write task-execution user code here
                    String textMessage = sendTextTextBox.getString();
                    comms.sendText(textMessage);
                    sendTextTextBox.setString("");
                    chatLogList.append("me: " + textMessage, null);
                }//GEN-BEGIN:|55-getter|2|55-postInit
            });//GEN-END:|55-getter|2|55-postInit
            // write post-init user code here
        }//GEN-BEGIN:|55-getter|3|
        return sendingTextTask;
    }
    //</editor-fold>//GEN-END:|55-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: recordAudioForm ">//GEN-BEGIN:|74-getter|0|74-preInit
    /**
     * Returns an initiliazed instance of recordAudioForm component.
     * @return the initialized component instance
     */
    public Form getRecordAudioForm() {
        if (recordAudioForm == null) {//GEN-END:|74-getter|0|74-preInit
            // write pre-init user code here
            recordAudioForm = new Form("Recording audio message");//GEN-BEGIN:|74-getter|1|74-postInit
            recordAudioForm.addCommand(getOkCommand());
            recordAudioForm.addCommand(getCancelCommand());
            recordAudioForm.setCommandListener(this);//GEN-END:|74-getter|1|74-postInit
            // write post-init user code here

        }//GEN-BEGIN:|74-getter|2|
        return recordAudioForm;
    }
    //</editor-fold>//GEN-END:|74-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: recordVideoForm ">//GEN-BEGIN:|75-getter|0|75-preInit
    /**
     * Returns an initiliazed instance of recordVideoForm component.
     * @return the initialized component instance
     */
    public Form getRecordVideoForm() {
        if (recordVideoForm == null) {//GEN-END:|75-getter|0|75-preInit
            // write pre-init user code here
            recordVideoForm = new Form("Recording video message");//GEN-BEGIN:|75-getter|1|75-postInit
            recordVideoForm.addCommand(getOkCommand());
            recordVideoForm.addCommand(getCancelCommand());
            recordVideoForm.setCommandListener(this);//GEN-END:|75-getter|1|75-postInit
            // write post-init user code here
            // TODO Somehow reset the control on next line, after it has been used once for playing video
            Item item = (Item)mVideoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, null);
            recordVideoForm.append(item);
        }//GEN-BEGIN:|75-getter|2|
        return recordVideoForm;
    }
    //</editor-fold>//GEN-END:|75-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendingAudioWaitScreen ">//GEN-BEGIN:|85-getter|0|85-preInit
    /**
     * Returns an initiliazed instance of sendingAudioWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getSendingAudioWaitScreen() {
        if (sendingAudioWaitScreen == null) {//GEN-END:|85-getter|0|85-preInit
            // write pre-init user code here
            sendingAudioWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|85-getter|1|85-postInit
            sendingAudioWaitScreen.setTitle("Sending audio");
            sendingAudioWaitScreen.setCommandListener(this);
            sendingAudioWaitScreen.setTask(getSendingAudioTask());//GEN-END:|85-getter|1|85-postInit
            // write post-init user code here
        }//GEN-BEGIN:|85-getter|2|
        return sendingAudioWaitScreen;
    }
    //</editor-fold>//GEN-END:|85-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendingVideoWaitScreen ">//GEN-BEGIN:|93-getter|0|93-preInit
    /**
     * Returns an initiliazed instance of sendingVideoWaitScreen component.
     * @return the initialized component instance
     */
    public WaitScreen getSendingVideoWaitScreen() {
        if (sendingVideoWaitScreen == null) {//GEN-END:|93-getter|0|93-preInit
            // write pre-init user code here
            sendingVideoWaitScreen = new WaitScreen(getDisplay());//GEN-BEGIN:|93-getter|1|93-postInit
            sendingVideoWaitScreen.setTitle("Sending video");
            sendingVideoWaitScreen.setCommandListener(this);
            sendingVideoWaitScreen.setTask(getSendingVideoTask());//GEN-END:|93-getter|1|93-postInit
            // write post-init user code here
        }//GEN-BEGIN:|93-getter|2|
        return sendingVideoWaitScreen;
    }
    //</editor-fold>//GEN-END:|93-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: cancelCommand ">//GEN-BEGIN:|76-getter|0|76-preInit
    /**
     * Returns an initiliazed instance of cancelCommand component.
     * @return the initialized component instance
     */
    public Command getCancelCommand() {
        if (cancelCommand == null) {//GEN-END:|76-getter|0|76-preInit
            // write pre-init user code here
            cancelCommand = new Command("Cancel", Command.CANCEL, 0);//GEN-LINE:|76-getter|1|76-postInit
            // write post-init user code here
        }//GEN-BEGIN:|76-getter|2|
        return cancelCommand;
    }
    //</editor-fold>//GEN-END:|76-getter|2|
    //</editor-fold>


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendingAudioTask ">//GEN-BEGIN:|88-getter|0|88-preInit
    /**
     * Returns an initiliazed instance of sendingAudioTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getSendingAudioTask() {
        if (sendingAudioTask == null) {//GEN-END:|88-getter|0|88-preInit
            // write pre-init user code here
            sendingAudioTask = new SimpleCancellableTask();//GEN-BEGIN:|88-getter|1|88-execute
            sendingAudioTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|88-getter|1|88-execute
                    // write task-execution user code here
                    mAudioRecordControl.stopRecord();
                    mAudioPlayer.stop();
                    audioMessageCache = audioOutputStream.toByteArray();
                    comms.sendAudio(audioMessageCache);
                    audioMessageCache = null; // clear cache after send
                    audioOutputStream.reset();
                    chatLogList.append("me: [audio]", null);
                }//GEN-BEGIN:|88-getter|2|88-postInit
            });//GEN-END:|88-getter|2|88-postInit
            // write post-init user code here
        }//GEN-BEGIN:|88-getter|3|
        return sendingAudioTask;
    }
    //</editor-fold>//GEN-END:|88-getter|3|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: sendingVideoTask ">//GEN-BEGIN:|96-getter|0|96-preInit
    /**
     * Returns an initiliazed instance of sendingVideoTask component.
     * @return the initialized component instance
     */
    public SimpleCancellableTask getSendingVideoTask() {
        if (sendingVideoTask == null) {//GEN-END:|96-getter|0|96-preInit
            // write pre-init user code here
            sendingVideoTask = new SimpleCancellableTask();//GEN-BEGIN:|96-getter|1|96-execute
            sendingVideoTask.setExecutable(new org.netbeans.microedition.util.Executable() {
                public void execute() throws Exception {//GEN-END:|96-getter|1|96-execute
                    // write task-execution user code here
                    mVideoRecordControl.stopRecord();
                    mVideoRecordControl.commit();
                    mVideoPlayer.stop();
                    videoMessageCache = videoOutputStream.toByteArray();
                    comms.sendVideo(videoMessageCache);
                    videoMessageCache = null; // clear cache after send
                    videoOutputStream.reset();
                    chatLogList.append("me: [video]", null);
                }//GEN-BEGIN:|96-getter|2|96-postInit
            });//GEN-END:|96-getter|2|96-postInit
            // write post-init user code here
        }//GEN-BEGIN:|96-getter|3|
        return sendingVideoTask;
    }
    //</editor-fold>//GEN-END:|96-getter|3|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: errorLogForm ">//GEN-BEGIN:|101-getter|0|101-preInit
    /**
     * Returns an initiliazed instance of errorLogForm component.
     * @return the initialized component instance
     */
    public Form getErrorLogForm() {
        if (errorLogForm == null) {//GEN-END:|101-getter|0|101-preInit
            // write pre-init user code here
            errorLogForm = new Form("Error log");//GEN-BEGIN:|101-getter|1|101-postInit
            errorLogForm.addCommand(getExitCommand());
            errorLogForm.setCommandListener(this);//GEN-END:|101-getter|1|101-postInit
            // write post-init user code here
        }//GEN-BEGIN:|101-getter|2|
        return errorLogForm;
    }
    //</editor-fold>//GEN-END:|101-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: playSelectedCommand ">//GEN-BEGIN:|106-getter|0|106-preInit
    /**
     * Returns an initiliazed instance of playSelectedCommand component.
     * @return the initialized component instance
     */
    public Command getPlaySelectedCommand() {
        if (playSelectedCommand == null) {//GEN-END:|106-getter|0|106-preInit
            // write pre-init user code here
            playSelectedCommand = new Command("Play selected", Command.ITEM, 0);//GEN-LINE:|106-getter|1|106-postInit
            // write post-init user code here
        }//GEN-BEGIN:|106-getter|2|
        return playSelectedCommand;
    }
    //</editor-fold>//GEN-END:|106-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: viewVideoForm ">//GEN-BEGIN:|108-getter|0|108-preInit
    /**
     * Returns an initiliazed instance of viewVideoForm component.
     * @return the initialized component instance
     */
    public Form getViewVideoForm() {
        if (viewVideoForm == null) {//GEN-END:|108-getter|0|108-preInit
            // write pre-init user code here
            viewVideoForm = new Form("Video message");//GEN-BEGIN:|108-getter|1|108-postInit
            viewVideoForm.addCommand(getOkCommand());
            viewVideoForm.setCommandListener(this);//GEN-END:|108-getter|1|108-postInit
//            try {
//                // write post-init user code here
//                mVideoControl.setDisplaySize(viewVideoForm.getWidth(), viewVideoForm.getHeight());
//            } catch (MediaException ex) {
//                log.error(ex);
//            }
            Item item = (Item)mVideoControl.initDisplayMode(VideoControl.USE_GUI_PRIMITIVE, null);
            viewVideoForm.append(item);
        }//GEN-BEGIN:|108-getter|2|
        return viewVideoForm;
    }
    //</editor-fold>//GEN-END:|108-getter|2|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay () {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable (null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet have been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet ();
        } else {
            initialize ();
            startMIDlet ();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

}
