package org.dimi3.postalboy

import androidx.compose.runtime.Composable
import io.github.vooft.compose.treeview.core.TreeView
import io.github.vooft.compose.treeview.core.node.Branch
import io.github.vooft.compose.treeview.core.node.Leaf
import io.github.vooft.compose.treeview.core.tree.Tree

@Composable
fun TreeViewExample() {
    // build tree structure
    val tree = Tree<String> {
        Branch("Mammalia") {
            Branch("Carnivora") {
                Branch("Canidae") {
                    Branch("Canis") {
                        Leaf("Wolf")
                        Leaf("Dog")
                    }
                }
                Branch("Felidae") {
                    Branch("Felis") {
                        Leaf("Cat")
                    }
                    Branch("Panthera") {
                        Leaf("Lion")
                    }
                }
            }
        }
    }

    // render the tree
    TreeView(tree)
}
