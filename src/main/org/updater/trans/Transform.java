package src.main.org.updater.trans;

import org.objectweb.asm.tree.ClassNode;

import src.main.org.updater.Updater;

import java.util.Collection;
import java.util.HashMap;

public abstract class Transform {
    public Updater instance;

    public Transform(Updater i) {
        instance = i;
    }

    /*
     * Analysis of classes, fields and methods
     */
    public abstract HashMap<String, ClassNode> identify(Collection<ClassNode> classNodes);

    /*
     * Modification of pre-existing classes, fields and methods.
     */
    public abstract HashMap<String, ClassNode> manipulate(Collection<ClassNode> classNodes);
}
