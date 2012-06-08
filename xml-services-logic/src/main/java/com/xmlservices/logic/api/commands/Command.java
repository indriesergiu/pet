package com.xmlservices.logic.api.commands;

/**
 * Command that can be applied on XML files.
 *
 * @author Sergiu Indrie
 */
public interface Command {

    void execute() throws Exception;
}
