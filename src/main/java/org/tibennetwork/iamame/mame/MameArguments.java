package org.tibennetwork.iamame.mame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

/**
 * Set of Mame command line arguments
 */
public class MameArguments {
    
    private static Options mameOptions;

    private static String[] commands = {
        "showusage",
        "showconfig",
        "listmedia",
        "createconfig",
        "help",
        "validate",
        "listxml",
        "listfull",
        "listsource",
        "listclones",
        "listbrothers",
        "listcrc",
        "listroms",
        "listsamples",
        "verifyroms",
        "verifysamples",
        "romident",
        "listdevices",
        "listslots",
        "listmedia",
        "listsoftware",
        "verifysoftware",
        "getsoftlist",
        "verifysoftlist"};

    private String[] rawArgs;

    private CommandLine commandLine;

    private MachineRepository machineRepository;

    private SoftwareRepository softwareRepository;

    private Machine machine;

    private List<Software> softwares = new ArrayList<>();

    public MameArguments (
            MachineRepository mr, 
            SoftwareRepository sr, 
            String[] rawArgs)
            throws InvalidMameArgumentsException, 
                        IOException, 
                        InterruptedException {
        
        this.machineRepository = mr;
        this.softwareRepository = sr;
        this.rawArgs = rawArgs;
        
        this.parseArgs();
    }
    
    public String[] getRawArgs () {
        return this.rawArgs;
    }

    public boolean hasMachine () {
        return this.machine != null;
    }

    public Machine getMachine () {
        return machine;
    }

    public List<Software> getSoftwares() {
        return softwares;
    }

    public boolean hasSoftwares () {
        return softwares != null;
    }

    /**
     * Tell whether the command line contains a 
     * Mame command (-listxml, -createconfig etc.)
     */
    public boolean containsCommand () {

        for (String optionName: commands) {
            if (this.commandLine.hasOption(optionName)) {
                return true;
            }
        }

        return false;
    }
            
    /**
     * Parse args in raw string and store usefull
     * Arguments.
     */
    private void parseArgs () 
            throws InvalidMameArgumentsException, 
                  IOException, 
                  InterruptedException {
        
        try {            
            CommandLineParser parser = new DefaultParser();
            this.commandLine = parser.parse(mameOptions, rawArgs);
        } catch (ParseException e) {
            throw (InvalidMameArgumentsException) 
                new InvalidMameArgumentsException(
                        "Error while parsing commandLine")
                    .initCause(e);
        }

        // Retrieve non option arguments
        // There must be at most 2 non arguments : 
        // <machine> [<software> | -<media> <software|softwarepath>]
        String[] cmdArgs = this.commandLine.getArgs();

        if (cmdArgs.length > 2) {
            throw new InvalidMameArgumentsException(
                    "Too many non option args");
        }

        if (cmdArgs.length >= 1) {
            try {
                this.machine = this.machineRepository
                    .findByName(cmdArgs[0]);
            } catch (MachineDoesntExistException e) {
                throw (InvalidMameArgumentsException) 
                    new InvalidMameArgumentsException(e.getMessage())
                        .initCause(e);
            }

            if (cmdArgs.length > 1) {

                if(this.isRegularSoftwareFile(cmdArgs[1])) {
                    throw new InvalidMameArgumentsException(
                        "The software must be a software name from software"
                            + " list and not a regular file");
                }

                try {
                    this.softwares.add(this.softwareRepository
                        .findByMachineAndByName(this.machine, cmdArgs[1]));
                } catch (MachineHasNoSoftwareListException 
                        | SoftwareNotFoundInSoftwareListsException 
                        | MachineDoesntExistException e) {
                    throw (InvalidMameArgumentsException) 
                        new InvalidMameArgumentsException(e.getMessage())
                            .initCause(e);
                }
            }
            else {
                for (MediaDevice md: this.machine.getMediaDevices()) {
                    if (this.commandLine.hasOption(md.getBriefname())) {

                        String softwareName  = this.commandLine
                            .getOptionValue(md.getBriefname());

                        Software s;

                        if(this.isRegularSoftwareFile(softwareName)) {
                            s = new Software(machine, md, softwareName);
                        } else {
                            try {
                                s = this.softwareRepository
                                    .findByMachineAndAndByMediaTypeAndByName(
                                        machine,
                                        md,
                                        softwareName);
                            } catch (MachineHasNoSoftwareListException 
                                    | SoftwareNotFoundInSoftwareListsException 
                                    | MachineDoesntExistException e) {
                                throw (InvalidMameArgumentsException) 
                                    new InvalidMameArgumentsException(
                                            e.getMessage())
                                        .initCause(e);
                            }
                        }
                        
                        this.softwares.add(s);

                    }
                }
            }
        }
    }

    /**
     * Determine whether the given file is a regular
     * software file (ie is not a softwarelist name)
     */
    private boolean isRegularSoftwareFile (String name) {
        return !FilenameUtils.getExtension(name).isEmpty()
                || new File(name).exists();
    }

    /**
     * Static constructor
     * Prepares the mameOptions arguments specification
     */
    static {
    
        mameOptions = new Options();

        mameOptions.addOption("readconfig", false, "");
        mameOptions.addOption("writeconfig", false, "");

        mameOptions.addOption("rompath", true, "");
        mameOptions.addOption("hashpath", true, "");
        mameOptions.addOption("samplepath", true, "");
        mameOptions.addOption("artpath", true, "");
        mameOptions.addOption("ctrlrpath", true, "");
        mameOptions.addOption("inipath", true, "");
        mameOptions.addOption("fontpath", true, "");
        mameOptions.addOption("cheatpath", true, "");
        mameOptions.addOption("crosshairpath", true, "");
        mameOptions.addOption("pluginspath", true, "");
        mameOptions.addOption("languagepath", true, "");
        mameOptions.addOption("cfg_directory", true, "");
        mameOptions.addOption("nvram_directory", true, "");
        mameOptions.addOption("input_directory", true, "");
        mameOptions.addOption("state_directory", true, "");
        mameOptions.addOption("snapshot_directory", true, "");
        mameOptions.addOption("diff_directory", true, "");
        mameOptions.addOption("comment_directory", true, "");
        mameOptions.addOption("state", true, "");
        mameOptions.addOption("autosave", false, "");
        mameOptions.addOption("playback", true, "");
        mameOptions.addOption("record", true, ""); //F
        mameOptions.addOption("record_timecode", true, "");
        mameOptions.addOption("exit_after_playback", false, "");
        mameOptions.addOption("mngwrite", true, "");
        mameOptions.addOption("aviwrite", true, "");
        mameOptions.addOption("wavwrite", true, "");
        mameOptions.addOption("snapname", true, "");
        mameOptions.addOption("snapsize", true, "");
        mameOptions.addOption("snapview", true, "");
        mameOptions.addOption("snapbilinear", false, "");
        mameOptions.addOption("statename", true, "");
        mameOptions.addOption("burnin", false, ""); //F

        mameOptions.addOption("autoframeskip", false, "");
        mameOptions.addOption("frameskip", true, "");
        mameOptions.addOption("seconds_to_run", true, "");
        mameOptions.addOption("throttle", false, "");
        mameOptions.addOption("sleep", false, "");
        mameOptions.addOption("speed", true, "");
        mameOptions.addOption("refreshspeed", false, "");
        mameOptions.addOption("rotate", false, "");
        mameOptions.addOption("ror", false, "");
        mameOptions.addOption("rol", false, "");
        mameOptions.addOption("autoror", false, "");
        mameOptions.addOption("autorol", false, "");
        mameOptions.addOption("flipx", false, "");
        mameOptions.addOption("flipy", false, "");

        mameOptions.addOption("artwork_crop", false, "");
        mameOptions.addOption("use_backdrops", false, "");
        mameOptions.addOption("use_overlays", false, "");
        mameOptions.addOption("use_bezels", false, "");
        mameOptions.addOption("use_cpanels", false, "");
        mameOptions.addOption("use_marquees", false, "");

        mameOptions.addOption("brightness", true, "");
        mameOptions.addOption("contrast", true, "");
        mameOptions.addOption("gamma", true, "");
        mameOptions.addOption("pause_brightness", true, "");
        mameOptions.addOption("effect", true, "");

        mameOptions.addOption("antialias", false, "");
        mameOptions.addOption("beam_width_min", true, "");
        mameOptions.addOption("beam_width_max", true, "");
        mameOptions.addOption("beam_intensity_weightset", true, "");
        mameOptions.addOption("flicker", false, "");
        
        mameOptions.addOption("samplerate", true, "");
        mameOptions.addOption("samples", false, "");
        mameOptions.addOption("volume", true, "");

        mameOptions.addOption("coin_lockout", false, "");
        mameOptions.addOption("ctrlr", false, ""); //F
        mameOptions.addOption("mouse", false, "");
        mameOptions.addOption("joystick", false, "");
        mameOptions.addOption("lightgun", false, "");
        mameOptions.addOption("multikeyboard", false, "");
        mameOptions.addOption("multimouse", false, "");
        mameOptions.addOption("steadykey", false, "");
        mameOptions.addOption("ui_active", false, "");
        mameOptions.addOption("offscreen_reload", false, "");
        mameOptions.addOption("joystick_map", true, ""); //F
        mameOptions.addOption("joystick_deadzone", true, "");
        mameOptions.addOption("joystick_saturation", true, "");
        mameOptions.addOption("natural", false, "");
        mameOptions.addOption("joystick_contradictoryenable", false, "");
        mameOptions.addOption("coin_impulse", true, "");

        mameOptions.addOption("paddle_device", false, "");
        mameOptions.addOption("adstick_device", false, "");
        mameOptions.addOption("pedal_device", false, "");
        mameOptions.addOption("dial_device", false, "");
        mameOptions.addOption("trackball_device", false, "");
        mameOptions.addOption("lightgun_device", false, "");
        mameOptions.addOption("positional_device", false, "");
        mameOptions.addOption("mouse_device", false, "");

        mameOptions.addOption("verbose", false, "");
        mameOptions.addOption("log", true, ""); //F 
        mameOptions.addOption("oslog", false, "");
        mameOptions.addOption("debug", false, "");
        mameOptions.addOption("update_in_pause", false, "");
        mameOptions.addOption("debugscript", true, "");

        mameOptions.addOption("comm_localhost", true, "");
        mameOptions.addOption("comm_localport", true, "");
        mameOptions.addOption("comm_remotehost", true, "");
        mameOptions.addOption("comm_remoteport", true, "");

        mameOptions.addOption("drc", false, "");
        mameOptions.addOption("drc_use_c", false, "");
        mameOptions.addOption("drc_log_uml", true, ""); //F
        mameOptions.addOption("drc_log_native", true, ""); //F
        mameOptions.addOption("bios", true, ""); //F
        mameOptions.addOption("cheat", false, "");
        mameOptions.addOption("skip_gameinfo", false, "");
        mameOptions.addOption("uifont", true, "");
        mameOptions.addOption("ui", true, "");
        mameOptions.addOption("ramsize", true, "");
        mameOptions.addOption("confirm_quit", false, "");
        mameOptions.addOption("ui_mouse", false, "");
        mameOptions.addOption("autoboot_command", true, "");
        mameOptions.addOption("autoboot_delay", true, "");
        mameOptions.addOption("autoboot_script", true, "");
        mameOptions.addOption("console", false, "");
        mameOptions.addOption("language", true, "");

        mameOptions.addOption("help", false, "");
        mameOptions.addOption("validate", false, "");

        mameOptions.addOption("createconfig", false, "");
        mameOptions.addOption("showconfig", false, "");
        mameOptions.addOption("showusage", false, "");

        mameOptions.addOption("listxml", false, "");
        mameOptions.addOption("listfull", false, "");
        mameOptions.addOption("listsource", false, "");
        mameOptions.addOption("listclones", false, "");
        mameOptions.addOption("listbrothers", false, "");
        mameOptions.addOption("listcrc", false, "");
        mameOptions.addOption("listroms", false, "");
        mameOptions.addOption("listsamples", false, "");
        mameOptions.addOption("verifyroms", false, "");
        mameOptions.addOption("verifysamples", false, "");
        mameOptions.addOption("romident", false, "");
        mameOptions.addOption("listdevices", false, "");
        mameOptions.addOption("listslots", false, "");
        mameOptions.addOption("listmedia", false, "");
        mameOptions.addOption("listsoftware", false, "");
        mameOptions.addOption("verifysoftware", false, "");
        mameOptions.addOption("getsoftlist", false, "");
        mameOptions.addOption("verifysoftlist", false, "");

        mameOptions.addOption("uimodekey", true, "");

        mameOptions.addOption("uifontprovider", true, "");

        mameOptions.addOption("listmidi", false, "");
        mameOptions.addOption("listnetwork", false, "");

        mameOptions.addOption("debugger", true, "");
        mameOptions.addOption("debugger_font", true, "");
        mameOptions.addOption("debugger_font_size", true, "");
        mameOptions.addOption("watchdog", true, "");
        
        mameOptions.addOption("multithreading", false, "");
        mameOptions.addOption("numprocessors", true, "");

        mameOptions.addOption("bench", false, "");

        mameOptions.addOption("video", true, "");
        mameOptions.addOption("numscreens", true, "");
        mameOptions.addOption("window", false, "");
        mameOptions.addOption("maximize", false, "");
        mameOptions.addOption("keepaspect", false, "");
        mameOptions.addOption("unevenstretch", false, "");
        mameOptions.addOption("waitvsync", false, "");
        mameOptions.addOption("syncrefresh", false, "");

        mameOptions.addOption("screen", true, "");
        mameOptions.addOption("aspect", true, "");
        mameOptions.addOption("resolution", true, "");
        mameOptions.addOption("view", true, "");
        mameOptions.addOption("screen0", true, "");
        mameOptions.addOption("aspect0", true, "");
        mameOptions.addOption("resolution0", true, "");
        mameOptions.addOption("view0", true, "");
        mameOptions.addOption("screen1", true, "");
        mameOptions.addOption("aspect1", true, "");
        mameOptions.addOption("resolution1", true, "");
        mameOptions.addOption("view1", true, "");
        mameOptions.addOption("screen2", true, "");
        mameOptions.addOption("aspect2", true, "");
        mameOptions.addOption("resolution2", true, "");
        mameOptions.addOption("view2", true, "");
        mameOptions.addOption("screen3", true, "");
        mameOptions.addOption("aspect3", true, "");
        mameOptions.addOption("resolution3", true, "");
        mameOptions.addOption("view3", true, "");
        
        mameOptions.addOption("switchres", false, "");

        mameOptions.addOption("filter", false, "");
        mameOptions.addOption("prescale", true, "");

        mameOptions.addOption("gl_forcepow2texture", false, "");
        mameOptions.addOption("gl_notexturerect", false, "");
        mameOptions.addOption("gl_vbo", false, "");
        mameOptions.addOption("gl_pbo", false, "");
        mameOptions.addOption("gl_glsl", false, "");
        mameOptions.addOption("gl_glsl_filter", false, "");

        mameOptions.addOption("glsl_shader_mame0", true, "");
        mameOptions.addOption("glsl_shader_mame1", true, "");
        mameOptions.addOption("glsl_shader_mame2", true, "");
        mameOptions.addOption("glsl_shader_mame3", true, "");
        mameOptions.addOption("glsl_shader_mame4", true, "");
        mameOptions.addOption("glsl_shader_mame5", true, "");
        mameOptions.addOption("glsl_shader_mame6", true, "");
        mameOptions.addOption("glsl_shader_mame7", true, "");
        mameOptions.addOption("glsl_shader_mame8", true, "");
        mameOptions.addOption("glsl_shader_mame9", true, "");
        mameOptions.addOption("glsl_shader_screen0", true, "");
        mameOptions.addOption("glsl_shader_screen1", true, "");
        mameOptions.addOption("glsl_shader_screen2", true, "");
        mameOptions.addOption("glsl_shader_screen3", true, "");
        mameOptions.addOption("glsl_shader_screen4", true, "");
        mameOptions.addOption("glsl_shader_screen5", true, "");
        mameOptions.addOption("glsl_shader_screen6", true, "");
        mameOptions.addOption("glsl_shader_screen7", true, "");
        mameOptions.addOption("glsl_shader_screen8", true, "");
        mameOptions.addOption("glsl_shader_screen9", true, "");

        mameOptions.addOption("sound", false, "");
        mameOptions.addOption("audio_latency", true, "");

        mameOptions.addOption("sdlvideofps", false, "");

        mameOptions.addOption("centerh", false, "");
        mameOptions.addOption("centerv", false, "");
        mameOptions.addOption("scalemode", true, "");

        mameOptions.addOption("useallheads", false, "");

        mameOptions.addOption("keymap", false, "");
        mameOptions.addOption("keymap_file", true, "");

        mameOptions.addOption("joy_idx1", true, "");
        mameOptions.addOption("joy_idx2", true, "");
        mameOptions.addOption("joy_idx3", true, "");
        mameOptions.addOption("joy_idx4", true, "");
        mameOptions.addOption("joy_idx5", true, "");
        mameOptions.addOption("joy_idx6", true, "");
        mameOptions.addOption("joy_idx7", true, "");
        mameOptions.addOption("joy_idx8", true, "");
        mameOptions.addOption("sixaxis", true, "");

        mameOptions.addOption("mouse_index1", true, "");
        mameOptions.addOption("mouse_index2", true, "");
        mameOptions.addOption("mouse_index3", true, "");
        mameOptions.addOption("mouse_index4", true, "");
        mameOptions.addOption("mouse_index5", true, "");
        mameOptions.addOption("mouse_index6", true, "");
        mameOptions.addOption("mouse_index7", true, "");
        mameOptions.addOption("mouse_index8", true, "");

        mameOptions.addOption("keyb_idx1", true, "");
        mameOptions.addOption("keyb_idx2", true, "");
        mameOptions.addOption("keyb_idx3", true, "");
        mameOptions.addOption("keyb_idx4", true, "");
        mameOptions.addOption("keyb_idx5", true, "");
        mameOptions.addOption("keyb_idx6", true, "");
        mameOptions.addOption("keyb_idx7", true, "");
        mameOptions.addOption("keyb_idx8", true, "");

        mameOptions.addOption("videodriver", true, "");
        mameOptions.addOption("renderdriver", true, "");
        mameOptions.addOption("audiodriver", true, "");
        mameOptions.addOption("gl_lib", true, "");

        // Media specific option args
        mameOptions.addOption("bitb", true, "");
        mameOptions.addOption("brief", true, "");
        mameOptions.addOption("card", true, "");
        mameOptions.addOption("card1", true, "");
        mameOptions.addOption("card10", true, "");
        mameOptions.addOption("card11", true, "");
        mameOptions.addOption("card12", true, "");
        mameOptions.addOption("card13", true, "");
        mameOptions.addOption("card14", true, "");
        mameOptions.addOption("card15", true, "");
        mameOptions.addOption("card16", true, "");
        mameOptions.addOption("card2", true, "");
        mameOptions.addOption("card3", true, "");
        mameOptions.addOption("card4", true, "");
        mameOptions.addOption("card5", true, "");
        mameOptions.addOption("card6", true, "");
        mameOptions.addOption("card7", true, "");
        mameOptions.addOption("card8", true, "");
        mameOptions.addOption("card9", true, "");
        mameOptions.addOption("cart", true, "");
        mameOptions.addOption("cart1", true, "");
        mameOptions.addOption("cart10", true, "");
        mameOptions.addOption("cart11", true, "");
        mameOptions.addOption("cart12", true, "");
        mameOptions.addOption("cart13", true, "");
        mameOptions.addOption("cart14", true, "");
        mameOptions.addOption("cart15", true, "");
        mameOptions.addOption("cart16", true, "");
        mameOptions.addOption("cart17", true, "");
        mameOptions.addOption("cart18", true, "");
        mameOptions.addOption("cart2", true, "");
        mameOptions.addOption("cart3", true, "");
        mameOptions.addOption("cart4", true, "");
        mameOptions.addOption("cart5", true, "");
        mameOptions.addOption("cart6", true, "");
        mameOptions.addOption("cart7", true, "");
        mameOptions.addOption("cart8", true, "");
        mameOptions.addOption("cart9", true, "");
        mameOptions.addOption("cass", true, "");
        mameOptions.addOption("cass1", true, "");
        mameOptions.addOption("cass2", true, "");
        mameOptions.addOption("cdrm", true, "");
        mameOptions.addOption("cdrm1", true, "");
        mameOptions.addOption("cdrm2", true, "");
        mameOptions.addOption("cdrm3", true, "");
        mameOptions.addOption("ct", true, "");
        mameOptions.addOption("cyln", true, "");
        mameOptions.addOption("disk1", true, "");
        mameOptions.addOption("disk2", true, "");
        mameOptions.addOption("dump", true, "");
        mameOptions.addOption("flop", true, "");
        mameOptions.addOption("flop1", true, "");
        mameOptions.addOption("flop2", true, "");
        mameOptions.addOption("flop3", true, "");
        mameOptions.addOption("flop4", true, "");
        mameOptions.addOption("flop5", true, "");
        mameOptions.addOption("flop6", true, "");
        mameOptions.addOption("hard", true, "");
        mameOptions.addOption("hard1", true, "");
        mameOptions.addOption("hard2", true, "");
        mameOptions.addOption("hard3", true, "");
        mameOptions.addOption("hard4", true, "");
        mameOptions.addOption("hard5", true, "");
        mameOptions.addOption("hard6", true, "");
        mameOptions.addOption("hard7", true, "");
        mameOptions.addOption("incart60p", true, "");
        mameOptions.addOption("magt", true, "");
        mameOptions.addOption("magt1", true, "");
        mameOptions.addOption("magt2", true, "");
        mameOptions.addOption("magt3", true, "");
        mameOptions.addOption("magt4", true, "");
        mameOptions.addOption("mc1", true, "");
        mameOptions.addOption("mc2", true, "");
        mameOptions.addOption("memc", true, "");
        mameOptions.addOption("min", true, "");
        mameOptions.addOption("mout", true, "");
        mameOptions.addOption("mout1", true, "");
        mameOptions.addOption("mout2", true, "");
        mameOptions.addOption("ni", true, "");
        mameOptions.addOption("p1", true, "");
        mameOptions.addOption("p2", true, "");
        mameOptions.addOption("prin", true, "");
        mameOptions.addOption("prin1", true, "");
        mameOptions.addOption("prin2", true, "");
        mameOptions.addOption("prin3", true, "");
        mameOptions.addOption("ptap1", true, "");
        mameOptions.addOption("ptap2", true, "");
        mameOptions.addOption("quik", true, "");
        mameOptions.addOption("quik1", true, "");
        mameOptions.addOption("quik2", true, "");
        mameOptions.addOption("sasi", true, "");
        mameOptions.addOption("serl", true, "");
    } 
}
