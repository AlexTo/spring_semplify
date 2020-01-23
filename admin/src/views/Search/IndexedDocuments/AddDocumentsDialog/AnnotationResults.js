import React, {Fragment} from "react";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow
} from '@material-ui/core';
import SelectTags from "./SelectTags";

function AnnotationResults({annotations}) {


  return (
    <Fragment>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell></TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {annotations.map(a => (
            <TableRow key={a.id}>
              <TableCell>
                {a.label}
              </TableCell>
              <TableCell>
                <SelectTags id={a.id} label={a.label} suggestedTags={a.resources}/>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Fragment>)
}

export default AnnotationResults
