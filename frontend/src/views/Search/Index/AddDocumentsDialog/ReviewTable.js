import React, {Fragment} from "react";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow
} from '@material-ui/core';
import SelectAnnotations from "./SelectAnnotations";

function ReviewTable({suggestedAnnotations, display, onAnnotationsSelected, onAnnotationAdded}) {

  return (
    <div style={{display: display}}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>Metadata</TableCell>
            <TableCell>Annotations</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {suggestedAnnotations.map(a => (
            <TableRow key={a.uri}>
              <TableCell>
                {a.label}
              </TableCell>
              <TableCell></TableCell>
              <TableCell>
                <SelectAnnotations label={a.label}
                                   suggestedAnnotations={a.resources}
                                   onAnnotationsSelected={(selectedAnnotations) => onAnnotationsSelected(a.uri, selectedAnnotations)}
                                   onAnnotationAdded={annotation => onAnnotationAdded(a.uri, annotation)}/>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>)
}

export default ReviewTable
