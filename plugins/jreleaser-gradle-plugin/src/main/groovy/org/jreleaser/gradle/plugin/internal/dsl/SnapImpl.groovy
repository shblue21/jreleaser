/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2022 The JReleaser authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jreleaser.gradle.plugin.internal.dsl

import groovy.transform.CompileStatic
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.internal.provider.Providers
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Internal
import org.jreleaser.gradle.plugin.dsl.CommitAuthor
import org.jreleaser.gradle.plugin.dsl.Snap
import org.jreleaser.gradle.plugin.dsl.Tap
import org.kordamp.gradle.util.ConfigureUtil

import javax.inject.Inject

import static org.jreleaser.util.JReleaserOutput.nag
import static org.jreleaser.util.StringUtils.isNotBlank

/**
 *
 * @author Andres Almiray
 * @since 0.1.0
 */
@CompileStatic
class SnapImpl extends AbstractRepositoryPackager implements Snap {
    final Property<String> packageName
    final Property<String> base
    final Property<String> grade
    final Property<String> confinement
    final RegularFileProperty exportedLogin
    final Property<Boolean> remoteBuild
    final TapImpl snap
    final CommitAuthorImpl commitAuthor
    final SetProperty<String> localPlugs
    final SetProperty<String> localSlots
    final NamedDomainObjectContainer<Plug> plugs
    final NamedDomainObjectContainer<Slot> slots

    private final NamedDomainObjectContainer<ArchitectureImpl> architectures

    @Inject
    SnapImpl(ObjectFactory objects) {
        super(objects)
        packageName = objects.property(String).convention(Providers.notDefined())
        base = objects.property(String).convention(Providers.notDefined())
        grade = objects.property(String).convention(Providers.notDefined())
        confinement = objects.property(String).convention(Providers.notDefined())
        exportedLogin = objects.fileProperty().convention(Providers.notDefined())
        remoteBuild = objects.property(Boolean).convention(Providers.notDefined())
        localPlugs = objects.setProperty(String).convention(Providers.notDefined())
        localSlots = objects.setProperty(String).convention(Providers.notDefined())
        snap = objects.newInstance(TapImpl, objects)
        commitAuthor = objects.newInstance(CommitAuthorImpl, objects)

        plugs = objects.domainObjectContainer(Plug, new NamedDomainObjectFactory<Plug>() {
            @Override
            Plug create(String name) {
                PlugImpl plug = objects.newInstance(PlugImpl, objects)
                plug.name = name
                return plug
            }
        })

        slots = objects.domainObjectContainer(Slot, new NamedDomainObjectFactory<Slot>() {
            @Override
            Slot create(String name) {
                SlotImpl slot = objects.newInstance(SlotImpl, objects)
                slot.name = name
                return slot
            }
        })

        architectures = objects.domainObjectContainer(ArchitectureImpl, new NamedDomainObjectFactory<ArchitectureImpl>() {
            @Override
            ArchitectureImpl create(String name) {
                ArchitectureImpl architecture = objects.newInstance(ArchitectureImpl, objects)
                architecture.name = name
                architecture
            }
        })
    }

    @Override
    @Deprecated
    void addLocalPlug(String plug) {
        nag('snap.addLocalPlug() has been deprecated since 1.0.0-M2 and will be removed in the future. Use snap.localPlug() instead')
        localPlug(plug)
    }

    @Override
    void localPlug(String plug) {
        if (isNotBlank(plug)) {
            localPlugs.add(plug.trim())
        }
    }

    @Override
    @Deprecated
    void addLocalSlot(String slot) {
        nag('snap.addLocalSlot() has been deprecated since 1.0.0-M2 and will be removed in the future. Use snap.localSlot() instead')
        localSlot(slot)
    }

    @Override
    void localSlot(String slot) {
        if (isNotBlank(slot)) {
            localSlots.add(slot.trim())
        }
    }

    @Override
    @Internal
    boolean isSet() {
        super.isSet() ||
            packageName.present ||
            base.present ||
            grade.present ||
            confinement.present ||
            exportedLogin.present ||
            remoteBuild.present ||
            localPlugs.present ||
            localSlots.present ||
            plugs.size() ||
            slots.size() ||
            snap.isSet() ||
            commitAuthor.isSet()
    }

    @Override
    void snap(Action<? super Tap> action) {
        action.execute(snap)
    }

    @Override
    void commitAuthor(Action<? super CommitAuthor> action) {
        action.execute(commitAuthor)
    }

    @Override
    void architecture(Action<? super Architecture> action) {
        action.execute(architectures.maybeCreate("architecture-${architectures.size()}".toString()))
    }

    @Override
    void plugs(Action<? super NamedDomainObjectContainer<Plug>> action) {
        action.execute(plugs)
    }

    @Override
    void slots(Action<? super NamedDomainObjectContainer<Slot>> action) {
        action.execute(slots)
    }

    @Override
    void plugs(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = NamedDomainObjectContainer) Closure<Void> action) {
        ConfigureUtil.configure(action, plugs)
    }

    @Override
    void commitAuthor(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = CommitAuthor) Closure<Void> action) {
        ConfigureUtil.configure(action, commitAuthor)
    }

    @Override
    void architecture(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = Architecture) Closure<Void> action) {
        ConfigureUtil.configure(action, architectures.maybeCreate("architecture-${architectures.size()}".toString()))
    }

    @Override
    void slots(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = NamedDomainObjectContainer) Closure<Void> action) {
        ConfigureUtil.configure(action, slots)
    }

    @Override
    void snap(@DelegatesTo(strategy = Closure.DELEGATE_FIRST, value = Tap) Closure<Void> action) {
        ConfigureUtil.configure(action, snap)
    }

    org.jreleaser.model.Snap toModel() {
        org.jreleaser.model.Snap packager = new org.jreleaser.model.Snap()
        fillPackagerProperties(packager)
        fillTemplatePackagerProperties(packager)
        if (snap.isSet()) packager.snap = snap.toSnapTap()
        if (packageName.present) packager.packageName = packageName.get()
        if (base.present) packager.base = base.get()
        if (grade.present) packager.grade = grade.get()
        if (confinement.present) packager.confinement = confinement.get()
        if (snap.isSet()) packager.snap = snap.toSnapTap()
        if (commitAuthor.isSet()) packager.commitAuthor = commitAuthor.toModel()
        if (exportedLogin.present) {
            packager.exportedLogin = exportedLogin.get().asFile.absolutePath
        }
        packager.remoteBuild = remoteBuild.getOrElse(false)
        packager.localPlugs = (Set<String>) localPlugs.getOrElse([] as Set<String>)
        packager.localSlots = (Set<String>) localSlots.getOrElse([] as Set<String>)
        packager.plugs.addAll(plugs.collect([]) { Plug plug ->
            ((PlugImpl) plug).toModel()
        } as Set<org.jreleaser.model.Snap.Plug>)
        packager.slots.addAll(slots.collect([]) { Slot slot ->
            ((SlotImpl) slot).toModel()
        } as Set<org.jreleaser.model.Snap.Slot>)
        for (ArchitectureImpl architecture : architectures) {
            packager.addArchitecture(architecture.toModel())
        }
        packager
    }

    @CompileStatic
    static class SlotImpl implements Slot {
        String name
        final MapProperty<String, String> attributes
        final ListProperty<String> reads
        final ListProperty<String> writes

        @Inject
        SlotImpl(ObjectFactory objects) {
            attributes = objects.mapProperty(String, String).convention([:])
            reads = objects.listProperty(String).convention(Providers.notDefined())
            writes = objects.listProperty(String).convention(Providers.notDefined())
        }

        void setName(String name) {
            this.name = name
        }

        @Override
        @Deprecated
        void addAttribute(String key, String value) {
            nag('plug.addAttribute() has been deprecated since 1.0.0-M2 and will be removed in the future. Use plug.attribute() instead')
            attribute(key, value)
        }

        @Override
        @Deprecated
        void addRead(String read) {
            nag('plug.addRead() has been deprecated since 1.0.0-M2 and will be removed in the future. Use plug.read() instead')
            this.read(read)
        }

        @Override
        @Deprecated
        void addWrite(String write) {
            nag('plug.addWrite() has been deprecated since 1.0.0-M2 and will be removed in the future. Use plug.write() instead')
            this.write(write)
        }

        @Override
        void attribute(String key, String value) {
            if (isNotBlank(key) && isNotBlank(value)) {
                attributes.put(key.trim(), value.trim())
            }
        }

        @Override
        void read(String read) {
            if (isNotBlank(read)) {
                reads.add(read.trim())
            }
        }

        @Override
        void write(String write) {
            if (isNotBlank(write)) {
                writes.add(write.trim())
            }
        }

        org.jreleaser.model.Snap.Slot toModel() {
            org.jreleaser.model.Snap.Slot slot = new org.jreleaser.model.Snap.Slot()
            slot.name = name
            slot.attributes.putAll(attributes.get())
            slot.reads = (List<String>) reads.getOrElse([])
            slot.writes = (List<String>) writes.getOrElse([])
            slot
        }
    }

    @CompileStatic
    static class PlugImpl implements Plug {
        String name
        final MapProperty<String, String> attributes
        final ListProperty<String> reads
        final ListProperty<String> writes

        @Inject
        PlugImpl(ObjectFactory objects) {
            attributes = objects.mapProperty(String, String).convention([:])
            reads = objects.listProperty(String).convention(Providers.notDefined())
            writes = objects.listProperty(String).convention(Providers.notDefined())
        }

        void setName(String name) {
            this.name = name
        }

        @Override
        @Deprecated
        void addAttribute(String key, String value) {
            nag('slot.addAttribute() has been deprecated since 1.0.0-M2 and will be removed in the future. Use slot.attribute() instead')
            attribute(key, value)
        }

        @Override
        @Deprecated
        void addRead(String read) {
            nag('slot.addRead() has been deprecated since 1.0.0-M2 and will be removed in the future. Use slot.read() instead')
            this.read(read)
        }

        @Override
        @Deprecated
        void addWrite(String write) {
            nag('slot.addWrite() has been deprecated since 1.0.0-M2 and will be removed in the future. Use slot.write() instead')
            this.write(write)
        }

        @Override
        void attribute(String key, String value) {
            if (isNotBlank(key) && isNotBlank(value)) {
                attributes.put(key.trim(), value.trim())
            }
        }

        @Override
        void read(String read) {
            if (isNotBlank(read)) {
                reads.add(read.trim())
            }
        }

        @Override
        void write(String write) {
            if (isNotBlank(write)) {
                writes.add(write.trim())
            }
        }

        org.jreleaser.model.Snap.Plug toModel() {
            org.jreleaser.model.Snap.Plug plug = new org.jreleaser.model.Snap.Plug()
            plug.name = name
            plug.attributes.putAll(attributes.get())
            plug.reads = (List<String>) reads.getOrElse([])
            plug.writes = (List<String>) writes.getOrElse([])
            plug
        }
    }

    @CompileStatic
    static class ArchitectureImpl implements Architecture {
        String name
        final ListProperty<String> buildOn
        final ListProperty<String> runOn
        final Property<Boolean> ignoreError

        @Inject
        ArchitectureImpl(ObjectFactory objects) {
            buildOn = objects.listProperty(String).convention(Providers.notDefined())
            runOn = objects.listProperty(String).convention(Providers.notDefined())
            ignoreError = objects.property(Boolean).convention(Providers.notDefined())
        }

        @Override
        @Deprecated
        void addRunOn(String str) {
            nag('architecture.addRunOn() has been deprecated since 1.0.0-M2 and will be removed in the future. Use architecture.runOn() instead')
            runOn(str)
        }

        @Override
        @Deprecated
        void addBuildOn(String str) {
            nag('architecture.addBuildOn() has been deprecated since 1.0.0-M2 and will be removed in the future. Use architecture.buildOn() instead')
            buildOn(str)
        }

        @Override
        void runOn(String str) {
            if (isNotBlank(str)) {
                runOn.add(str.trim())
            }
        }

        @Override
        void buildOn(String str) {
            if (isNotBlank(str)) {
                buildOn.add(str.trim())
            }
        }

        org.jreleaser.model.Snap.Architecture toModel() {
            org.jreleaser.model.Snap.Architecture architecture = new org.jreleaser.model.Snap.Architecture()
            architecture.buildOn = (List<String>) buildOn.getOrElse([])
            architecture.runOn = (List<String>) runOn.getOrElse([])
            architecture.ignoreError = ignoreError.getOrElse(false)
            architecture
        }
    }
}
